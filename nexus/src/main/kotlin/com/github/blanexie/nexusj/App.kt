package com.github.blanexie.nexusj

import com.github.blanexie.nexusj.controller.nexusj
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import java.text.DateFormat


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.nexusj(testing: Boolean = true) {
    routing {
        install(AutoHeadResponse)
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        nexusj()
    }
}