package com.github.blanexie.nexusj

import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.setting.Setting
import com.github.blanexie.dao.UserDO
import com.github.blanexie.dao.gson
import com.github.blanexie.nexusj.controller.auth
import com.github.blanexie.nexusj.controller.notAuth
import com.github.blanexie.nexusj.support.SimpleJWT
import com.github.blanexie.nexusj.support.UserPrincipal
import com.github.blanexie.nexusj.support.jwtDecode
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import java.text.DateFormat


val setting = Setting(ClassPathResource(System.getProperty("properties.path") ?: "app.properties").path)

fun main(args: Array<String>): Unit {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.nexus(testing: Boolean = true) {
    val simpleJWT = SimpleJWT()
    install(AutoHeadResponse)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        jwt {
            verifier(simpleJWT.verifier)
            this.realm = simpleJWT.realm()
            validate { credential ->
                if (credential.payload.audience.contains(simpleJWT.audience())) {
                    val subject = credential.payload.subject
                    val userDO = gson.fromJson(jwtDecode(subject), UserDO::class.java)
                    UserPrincipal(userDO)
                } else {
                    null
                }
            }
        }
    }

    routing {
        route(("/api/nexus")) {
            authenticate {
                auth()
            }
            notAuth()
        }
    }
}

