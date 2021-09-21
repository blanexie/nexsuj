package com.github.blanexie.tracker

import com.github.blanexie.tracker.server.announce
import com.github.blanexie.tracker.server.model.TrackerResp
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import kotlinx.serialization.json.Json


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    routing {
        install(AutoHeadResponse)
        install(Compression)
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        get("/") {
            val infoHash = call.request.queryParameters["infoHash"].toString()
            if (infoHash == null) {
                call.respond(TrackerResp(null, null, null, null, "403"))
                return@get
            }
        }
    }
}