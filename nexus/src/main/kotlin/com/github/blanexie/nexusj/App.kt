package com.github.blanexie.nexusj

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.json.JSONUtil
import cn.hutool.setting.Setting
import com.github.blanexie.dao.UserDO
import com.github.blanexie.nexusj.controller.auth
import com.github.blanexie.nexusj.controller.notAuth
import com.github.blanexie.nexusj.support.SimpleJWT
import com.github.blanexie.nexusj.support.UserPrincipal
import com.github.blanexie.nexusj.support.jwtDecode
import com.github.blanexie.tracker.server.tracker
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import java.text.DateFormat


val gson: Gson = GsonBuilder().setDateFormat(DateFormat.LONG).create()!!
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
                    val jwtDecode = jwtDecode(subject)
                    val userDO = UserDO()

                    val parseObj = JSONUtil.parseObj(jwtDecode)
                    BeanUtil.fillBeanWithMap(parseObj, userDO, true)

                    // val userDO = gson.fromJson(, UserDO::class.java)
                    UserPrincipal(userDO)
                } else {
                    null
                }
            }
        }
    }

    routing {
        route(("/")) {
            tracker()
        }
        route(("/api/nexus")) {
            authenticate {
                auth()
            }
            notAuth()
        }
    }
}

