package com.github.blanexie.tracker.server

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.announce() {
    get("announce") {
        val infoHash = call.request.queryParameters["info_hash"]!!
        val peerId = call.request.queryParameters["peer_id"]!!
        val port = call.request.queryParameters["port"]!!.toInt()
        val ip = call.request.queryParameters["ip"]!!
        val uploaded = call.request.queryParameters["uploaded"]!!.toLong()
        val downloaded = call.request.queryParameters["downloaded"]!!.toLong()
        val left = call.request.queryParameters["left"]!!.toLong()
        val trackerId = call.request.queryParameters["trackerid"]
        val numwant = call.request.queryParameters["numwant"]?.toInt()
        val event = call.request.queryParameters["event"]

       val req = TrackerReq(infoHash, peerId, ip, port, uploaded, downloaded, left, event, 0, trackerId, numwant)



        call.respondText("hello word")
    }

}