package com.github.blanexie.tracker

import com.github.blanexie.tracker.server.announce
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = true) {
    routing {
        install(AutoHeadResponse)
        install(Compression)
        announce()
    }
}