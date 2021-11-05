package com.github.blanexie.tracker.server

import com.github.blanexie.dao.*
import com.github.blanexie.nexusj.support.event.eventBus
import com.github.blanexie.nexusj.support.event.uploadBytes
import com.github.blanexie.nexusj.controller.param.TrackerReq
import com.github.blanexie.nexusj.bencode.BeObj
import com.github.blanexie.nexusj.support.database
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.entity.*
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.time.LocalDateTime


val log = LoggerFactory.getLogger("announce")!!


fun Route.tracker() {

    get("/announce") {
        val trackerReq = TrackerReq(call.request)
        // 0. 检查访问的客户端是否符合要求
        val errorMsg = blockBrowser(call.request)
        if (errorMsg != null) {
            call.respond(BeObj(hashMapOf("failReason" to errorMsg)).toBen())
            return@get
        }

        // 1. 检查用户是否有当前种子的权限
        val userTorrentDO =
            database.userTorrentDO.findLast { (it.authKey eq trackerReq.authKey) and (it.infoHash eq trackerReq.infoHash) }
        if (userTorrentDO == null) {
            call.respond(BeObj(hashMapOf("failReason" to "你还没有下载这个种子无法开始下载")).toBen())
            return@get
        }

        // 2. 找出符合的peer返回
        val peers = database.peerDO.filter {
            (it.infoHash eq trackerReq.infoHash) and (it.event inList listOf(
                "completed",
                "started",
                "empty"
            ))
        }.map { it }

        // 2.1 找出本机peer
        var peer: PeerDO? = peers.findLast { it.userId == userTorrentDO.userId }
        if (peer == null) {
            peer = PeerDO.build(trackerReq, userTorrentDO.userId)
            database.peerDO.add(peer)
        } else {
            if (peer.peerId != trackerReq.peerId) {
                call.respond(BeObj(hashMapOf("failReason" to "一个种子只能一个客户端下载")).toBen())
                return@get
            } else {
                peer.downloaded = trackerReq.downloaded
                peer.uploaded = trackerReq.uploaded
                peer.left = trackerReq.left
                peer.event = trackerReq.event
                peer.compact = trackerReq.compact
                peer.lastReportTime = LocalDateTime.now()
                database.peerDO.update(peer)
            }
        }

        //2.2 返回数据
        val peersStr = peers.filter { it.peerId != peer.peerId }.map { getCompactPeer(it.ip, it.port) }
            .joinToString(separator = "")
        val count = peers.filter { it.event == "completed" }.count()
        val resp = hashMapOf<String, Any>()
        resp["interval"] = 3600
        resp["min interval"] = 30
        resp["incomplete"] = peers.size - count
        resp["complete"] = count
        resp["peers"] = peersStr

        //3. 上报用户下载和上传的数据
        eventBus.publish(uploadBytes, trackerReq)

        call.respondBytes(BeObj(resp).toBen(), contentType = ContentType.parse("text/plain"))
    }

}

fun getCompactPeer(ip: String, port: Int): ByteArray {
    val map = ip.split(".").map { it.toByte() }

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