package com.github.blanexie.tracker.server

import com.github.blanexie.tracker.bencode.BeInt
import com.github.blanexie.tracker.bencode.BeObj
import com.github.blanexie.tracker.bencode.BeStr
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.net.InetAddress

import io.netty.util.NetUtil.LOCALHOST
import org.slf4j.LoggerFactory
import java.net.UnknownHostException


val log = LoggerFactory.getLogger("announce")!!

fun Route.announce() {


    val peerMap = hashMapOf<String, TrackerReq>()

    get("announce") {

        val infoHash = call.request.queryParameters["info_hash"]!!
        val peerId = call.request.queryParameters["peer_id"]!!
        val port = call.request.queryParameters["port"]!!.toInt()
        val uploaded = call.request.queryParameters["uploaded"]!!.toLong()
        val downloaded = call.request.queryParameters["downloaded"]!!.toLong()
        val left = call.request.queryParameters["left"]!!.toLong()
        val trackerId = call.request.queryParameters["trackerid"]
        val numwant = call.request.queryParameters["numwant"]?.toInt()
        val event = call.request.queryParameters["event"]
        var ip = call.request.queryParameters["ip"]
        var compact = call.request.queryParameters["compact"]?.toInt()

        if (ip == null) {
            ip = getIpAddress(call.request)
        }


       val req =  peerMap[infoHash]

        val resp = hashMapOf<BeStr, BeObj>()
        resp[BeStr("interval")] = BeInt(3600)
        resp[BeStr("min interval")] = BeInt(30)
        resp[BeStr("incomplete")] = BeInt(0)
        resp[BeStr("complete")] = BeInt(1)

        req?.let {
            val toByteArray = it.ip!!.split(",").map { it.toByte() }.toByteArray()
            val ushr = it.port.ushr(4).toByte()


        }


        resp[BeStr("peers")] = BeInt(1)

        peerMap[infoHash] =
            TrackerReq(infoHash, peerId, port, uploaded, downloaded, left, event, ip, compact, trackerId, numwant)

        call.respondText("hello word")
    }

}

const val UNKNOWN = "unknown";

fun getIpAddress(request: ApplicationRequest): String? {
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