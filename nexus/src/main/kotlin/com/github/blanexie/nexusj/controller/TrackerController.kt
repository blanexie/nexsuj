package com.github.blanexie.nexusj.controller

import cn.hutool.core.net.URLDecoder
import cn.hutool.core.util.HexUtil
import cn.hutool.core.util.URLUtil
import cn.hutool.crypto.digest.DigestUtil
import com.github.blanexie.dao.PeerDO
import com.github.blanexie.dao.peerDO
import com.github.blanexie.dao.userTorrentDO
import com.github.blanexie.nexusj.bencode.bencode
import com.github.blanexie.nexusj.support.database
import com.github.blanexie.nexusj.support.event.eventBus
import com.github.blanexie.nexusj.support.event.uploadBytes
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.entity.*
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.time.LocalDateTime


val log = LoggerFactory.getLogger("announce")!!

val peerEvent = listOf(
    "completed",
    "started",
    "empty"
);

fun Route.tracker() {

    get("/announce") {
        // 0. 检查访问的客户端是否符合要求
        val errorMsg = blockBrowser(call.request)
        if (errorMsg != null) {
            call.respond(bencode.encode(hashMapOf("failReason" to errorMsg)))
            return@get
        }
        // 1： 构建peer对象， 并检查用户是否有当前种子的权限
        val peerDO = getPeer(call.request)
        val userTorrentDO =
            database.userTorrentDO.findLast { (it.authKey eq peerDO.authKey) and (it.infoHash eq peerDO.infoHash) }
        if (userTorrentDO == null) {
            call.respond(bencode.encode(hashMapOf("failReason" to "Operation without permission")))
            return@get
        }
        peerDO.userId = userTorrentDO.userId

        // 2. 找出符合的peer返回
        val peers = database.peerDO.filter {
            (it.infoHash eq peerDO.infoHash) and (it.event inList peerEvent)
        }.map { it }

        // 2.1 找出本机peer
        var peer: PeerDO? = peers.findLast { it.userId == peerDO.userId }
        if (peer == null) {
            database.peerDO.add(peerDO)
        } else {
            if (peer.peerId != peerDO.peerId) {
                call.respond(bencode.encode(hashMapOf("failReason" to "一个种子只能一个客户端下载")))
                return@get
            } else {
                peerDO.id = peer.id
                database.peerDO.update(peer)
            }
        }

        //2.2 返回数据
        val peersStr = peers.filter { it.peerId != peerDO.peerId }.map { getCompactPeer(it.ip, it.port) }
            .joinToString(separator = "")
        val count = peers.filter { it.event == "completed" }.count()
        val resp = hashMapOf<String, Any>()
        resp["interval"] = 3600
        resp["min interval"] = 30
        resp["incomplete"] = peers.size - count
        resp["complete"] = count
        resp["peers"] = peersStr

        //3. 上报用户下载和上传的数据
        eventBus.publish(uploadBytes, peerDO)

        call.respondBytes(bencode.encode(resp), contentType = ContentType.parse("text/plain"))
    }

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


fun getIpAddress(request: ApplicationRequest): String? {
    val UNKNOWN = "unknown";
    val LOCALHOST = "127.0.0.1"
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
        if (LOCALHOST == ipAddress) {
            var inet: InetAddress? = InetAddress.getLocalHost()
            ipAddress = inet!!.hostAddress
        }
    }

    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    // "***.***.***.***".length()
    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    // "***.***.***.***".length()
    if (ipAddress != null && ipAddress.length > 15) {
        if (ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","))
        }
    }
    return ipAddress
}


fun getPeer(request: ApplicationRequest): PeerDO {
    val peer = PeerDO()
    val infoHash = request.uri.substringAfter("info_hash=").substringBefore("&")
///announce?auth_key=e934b04ba3234d639c0523f0cb15fe0q&info_hash=%ce%cf%dc%0c%07%5d%22x%02%a79%0a-%ea%14%cb%a3s%af%eb&peer_id=-qB4380-mB)0B.0nhN6V&port=8110&uploaded=0&downloaded=0&left=0&corrupt=0&key=06825FE3&event=started&numwant=200&compact=1&no_peer_id=1&supportcrypto=1&redundant=0&ipv6=2409%3a8a28%3ac6c%3ad80%3a3881%3ae173%3a5a18%3a3a79&ipv6=2409%3a8a28%3ac6c%3ad80%3a540d%3a17db%3a840f%3a5f9f
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
    var compact = request.queryParameters["compact"]?.toInt()
    if (compact != null) {
        peer.compact = compact
    } else {
        peer.compact = 1
    }

    var event = request.queryParameters["event"]
    if (event == null) {
        peer.event = "empty"
    } else {
        peer.event = event
    }
    var ip = request.queryParameters["ip"]?.toString()
    if (ip == null) {
        peer.ip = getIpAddress(request)!!
    } else {
        peer.ip = ip
    }

    peer.ipv6 = request.queryParameters["ipv6"]?.toString()

    var numwant = request.queryParameters["numwant"]?.toInt()//: 可选的期望Tracker最大返回数.缺省为50个
    if (numwant == null) {
        peer.numwant = 50
    } else {
        peer.numwant = numwant
    }

    peer.authKey = request.queryParameters["auth_key"]!!
    peer.trackerid = request.queryParameters["trackerid"]?.toString()
    peer.createTime = LocalDateTime.now()
    //   peer.userId = userId
    peer.reportTime = LocalDateTime.now()
    return peer
}