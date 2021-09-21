package com.github.blanexie.tracker.server

import com.github.blanexie.tracker.server.model.Event
import com.github.blanexie.tracker.server.model.TrackerReq
import com.github.blanexie.tracker.server.model.TrackerResp
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json


fun Route.announce() {

    get("s") {

    }
//
//            val trackerReq =  TrackerReq(
//               !!,
//                queryParameters["peerId"]?,
//                queryParameters["left"]!!.toLong(),
//                queryParameters["numwant"]?.toInt(),
//                queryParameters["uploaded"]!!.toLong(),
//                queryParameters["downloaded"]!!.toLong(),
//                queryParameters["ip"]!!.toString(),
//                Event.valueOf(queryParameters["event"])??
//
//
//                )


}


