package com.github.blanexie.nexusj.controller

import cn.hutool.core.net.URLDecoder
import cn.hutool.core.util.HexUtil
import com.github.blanexie.nexusj.bencode.bencode
import com.github.blanexie.nexusj.support.event.eventBus
import com.github.blanexie.nexusj.support.event.uploadBytes
import com.github.blanexie.nexusj.support.gson
import com.github.blanexie.nexusj.table.PeerDO
import com.github.blanexie.nexusj.table.UserTorrentDO
import com.google.gson.reflect.TypeToken
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.netty.buffer.Unpooled
import org.slf4j.LoggerFactory
import java.lang.reflect.Type
import java.net.InetAddress
import java.time.LocalDateTime

val log = LoggerFactory.getLogger("announce")!!

val peerEvent = listOf(
    "completed",
    "started",
    "empty"
);

fun Route.tracker() {

    get() {
        // 0. 检查访问的客户端是否符合要求
        val errorMsg = blockBrowser(call.request)
        if (errorMsg != null) {
            call.respond(bencode.encode(hashMapOf("failReason" to errorMsg)))
            return@get
        }

        // 1： 构建peer对象， 并检查用户是否有当前种子的权限
        val peer = getPeer(call.request)
        // 1.1 校验ip正确与否
        if (peer.ip == null && peer.ipv6 == null) {
            call.respond(bencode.encode(hashMapOf("failReason" to "unknown peer ip")))
            return@get
        }
        //1.2 完善peer中的userId信息
        val userTorrentDO = UserTorrentDO.findByInfoHashAndAuthKey(peer.infoHash, peer.authKey)
        if (userTorrentDO == null) {
            call.respond(bencode.encode(hashMapOf("failReason" to "Operation without permission")))
            return@get
        }

        // 2. 找出符合的peer返回
        val peerDOs = PeerDO.findByInfoHashAndEventIn(peer.infoHash, peerEvent)
        // 2.1 找出本机peer
        var peerDO: PeerDO? = peerDOs.findLast { it.authKey == peer.authKey }
        if (peerDO == null) {
            peer.save()
        } else {
            // 2.2 查看当前用户是否有两个客户端下载
            if (peer.peerId != peerDO.peerId) {
                call.respond(bencode.encode(hashMapOf("failReason" to "too many client")))
                return@get
            } else {
                peer.id = peerDO.id
                peer.update()
            }
        }


        //2.2 返回数据
        val count = peerDOs.filter { it.event == "completed" }.count()
        val resp = hashMapOf<String, Any>()
        resp["interval"] = 3600
        resp["min interval"] = 30
        resp["incomplete"] = peerDOs.size - count
        resp["complete"] = count


        val bytebuf = Unpooled.buffer()
        peerDOs
            .filter { it.peerId != peer.peerId }
            .filter { it.ip != null }.filterNotNull()
            .map { getCompactPeer(it.ip!!, it.port) }
            .forEach { bytes -> bytebuf.writeBytes(bytes) }

        val nioBuffer = bytebuf.nioBuffer()
        if (bytebuf.readableBytes() > 0) {
            resp["peers"] = nioBuffer
        }

        val bytebuf6 = Unpooled.buffer()
        peerDOs
            .filter { it.peerId != peer.peerId }
            .filter { it.ipv6 != null }.filterNotNull()
            .map {
                val type: Type = object : TypeToken<List<String>>() {}.type
                val list: List<String> = gson.fromJson(it.ipv6, type)
                list.map { str -> getCompactPeer6(str, it.port) }
                    .reduce { acc, bytes -> acc.plus(bytes) }
            }
            .forEach { it -> bytebuf6.writeBytes(it) }
        if (bytebuf6.readableBytes() > 0) {
            resp["peers6"] = bytebuf6.nioBuffer()
        }

        //3. 上报用户下载和上传的数据
        eventBus.publish(uploadBytes, peer)

        call.respondBytes(bencode.encode(resp), contentType = ContentType.parse("text/plain"))
    }
}

private fun compactIpv6(ipv6: String): ByteArray {
    if (ipv6.startsWith("::")) {
        val split = ipv6.split("::")
        val s2 = split[1]
        val decodeHex1 = HexUtil.decodeHex(s2)
        val i = 16 - -decodeHex1.size
        return ByteArray(i).plus(decodeHex1)
    }
    if (ipv6.endsWith("::")) {
        val split = ipv6.split("::")
        val s2 = split[0]
        val decodeHex1 = HexUtil.decodeHex(s2)
        val i = 16 - -decodeHex1.size
        return decodeHex1.plus(ByteArray(i))
    }
    if (ipv6.contains("::")) {
        val split = ipv6.split("::")
        val s1 = split[0]
        val s2 = split[1]
        val decodeHex = HexUtil.decodeHex(s1)
        val decodeHex1 = HexUtil.decodeHex(s2)
        val i = 16 - decodeHex.size - decodeHex1.size
        return decodeHex.plus(ByteArray(i)).plus(decodeHex1)
    }

    val split = ipv6.split(":")
    return split.map { HexUtil.decodeHex(it) }.reduce { acc, bytes -> acc.plus(bytes) }
}

fun getCompactPeer6(ipv6: String, port: Int): ByteArray {
    val ipv6Bytes = compactIpv6(ipv6)
    val p1 = port ushr 8
    val p2 = port and 255
    return ipv6Bytes.plus(p1.toByte()).plus(p2.toByte())
}

fun getCompactPeer(ip: String, port: Int): ByteArray {
    val map = ip.split(".").map { it.toInt().toByte() }
    val p1 = port ushr 8
    val p2 = port and 255
    val plus = map.plus(p1.toByte()).plus(p2.toByte())
    return plus.toByteArray()
}


fun blockBrowser(request: ApplicationRequest): String? {
    val userAgent = request.userAgent()
    if (userAgent != null) {
        val acceptUserAgent = userAgent.contains("/^Mozilla/") || userAgent.contains("/^Opera/")
                || userAgent.contains("/^Links/") || userAgent.contains("/^Lynx/")
        if (acceptUserAgent) {
            return "Browser access blocked!"
        }
    }
    return null
}


val LOCALHOST = "127.0.0.1"
val LOCALHOSTIPV6 = "0:0:0:0:0:0:0:1"
val LOCALHOSTStr = "localhost"
val DOCKERLOCLHOST = "host.docker.internal"

/**
 * 注意对ipv6地址的兼容和处理
 */
fun getIpAddress(request: ApplicationRequest): String? {
    val UNKNOWN = "unknown";
    var ipAddress = request.header("x-forwarded-for")
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN != ipAddress) {
        ipAddress = request.header("Proxy-Client-IP")
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN != ipAddress) {
        ipAddress = request.header("WL-Proxy-Client-IP")
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN != ipAddress) {
        ipAddress = request.header("HTTP_CLIENT_IP");
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN != ipAddress) {
        ipAddress = request.header("HTTP_X_FORWARDED_FOR");
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN != ipAddress) {
        ipAddress = request.header("X-Real-IP");
    }
    if (ipAddress == null || ipAddress.isEmpty() || UNKNOWN != ipAddress) {
        ipAddress = request.origin.remoteHost
        if (LOCALHOST == ipAddress
            || LOCALHOSTStr == ipAddress
            || LOCALHOSTIPV6 == ipAddress
            || DOCKERLOCLHOST == ipAddress
        ) {
            var inet: InetAddress? = InetAddress.getLocalHost()
            ipAddress = inet!!.hostAddress
        }
    }

    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    // "***.***.***.***".length()
    if (ipAddress != null && ipAddress.length > 15) {
        if (ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
        }
    }
    return ipAddress
}

///announce
// ?auth_key=e934b04ba3234d639c0523f0cb15fe0q
// &info_hash=%ce%cf%dc%0c%07%5d%22x%02%a79%0a-%ea%14%cb%a3s%af%eb
// &peer_id=-qB4380-mB)0B.0nhN6V
// &port=8110
// &uploaded=0
// &downloaded=0
// &left=0
// &corrupt=0
// &key=06825FE3
// &event=started
// &numwant=200
// &compact=1
// &no_peer_id=1
// &supportcrypto=1
// &redundant=0
// &ipv6=2409%3a8a28%3ac6c%3ad80%3a3881%3ae173%3a5a18%3a3a79
// &ipv6=2409%3a8a28%3ac6c%3ad80%3a540d%3a17db%3a840f%3a5f9f
fun getPeer(request: ApplicationRequest): PeerDO {
    val peer = PeerDO()
    val infoHash = request.uri.substringAfter("info_hash=").substringBefore("&")
    val decode = URLDecoder.decode(infoHash.toByteArray(charset("utf8")))
    peer.infoHash = HexUtil.encodeHexStr(decode)
    peer.peerId = request.queryParameters["peer_id"]!!
    peer.port = request.queryParameters["port"]!!.toInt()
    peer.uploaded = request.queryParameters["uploaded"]!!.toLong()
    peer.downloaded = request.queryParameters["downloaded"]!!.toLong()
    peer.left = request.queryParameters["left"]!!.toLong()
    // 此参数值为1,表示期望得到紧凑模式的节点列表.
    // 否则表示期望得到普通模式的节点列表.
    // 指出客户端是否支持压缩模式. 如果是,伙伴列表将被一个伙伴字符串所代替.每个伙伴占6个字节.前4个字节是主机(网络字序) , 后2个字节是端口(网络字序).
    peer.compact = request.queryParameters["compact"].let { it?.toInt() ?: 1 }

    peer.event = request.queryParameters["event"].let {
        if (peer.left == 0L) "completed" else it ?: "empty"
    }

    //处理peer的上报地址
    peer.ip = request.queryParameters["ip"].let { it ?: getIpAddress(request)!! }.let {
        if (it != LOCALHOST) it else null
    }
    val ipv6 = request.queryParameters.getAll("ipv6")
    peer.ipv6 = ipv6?.filter { it != LOCALHOSTIPV6 }?.let { gson.toJson(it) }

    //  可选的期望Tracker最大返回数.缺省为50个
    peer.numwant = request.queryParameters["numwant"].let { it?.toInt() ?: 50 }
    peer.authKey = request.queryParameters["auth_key"]!!
    peer.trackerid = request.queryParameters["trackerid"]
    peer.createTime = LocalDateTime.now()
    peer.reportTime = LocalDateTime.now()

    return peer
}





