package com.github.blanexie.tracker

import com.github.blanexie.tracker.server.tracker
import io.ktor.application.*
import io.ktor.routing.*

fun Application.tracker(testing: Boolean = false) {
    routing {
        tracker()
    }
}