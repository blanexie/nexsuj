package com.github.blanexie.nexusj.controller

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.nexusj() {
    /**
     * 上传种子文件
     */
    get("upload") {
        call.respondText { "hello word" }
    }
}
