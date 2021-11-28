package com.github.blanexie.nexusj.support

import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.json.JSONUtil
import cn.hutool.setting.Setting
import com.github.blanexie.dao.UserDO
import com.github.blanexie.nexusj.controller.auth
import com.github.blanexie.nexusj.controller.notAuth
import com.github.blanexie.nexusj.controller.tracker
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.routing.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val setting = Setting(ClassPathResource(System.getProperty("properties.path") ?: "app.properties").path)
val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")!!
val simpleJWT = SimpleJWT()

fun buildUserPrincipal(jwtDecode: String): UserPrincipal {
    val fromJson = JSONUtil.parseObj(jwtDecode)
    val userDO = UserDO()
    fromJson.forEach { t, u ->
        run {
            userDO[t] = u
        }
    }
    return UserPrincipal(userDO)
}

fun Application.nexus(testing: Boolean = true) {

    install(AutoHeadResponse)
    install(Compression)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            registerTypeAdapter(LocalDateTime::class.java, JsonDeserializerImpl())
            registerTypeAdapter(LocalDateTime::class.java, JsonSerializerImpl())
            setPrettyPrinting()
        }
    }
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("X-Token")
        allowCredentials = true
        allowNonSimpleContentTypes = true
        anyHost()
    }
    install(Authentication) {
        jwt {
            verifier(simpleJWT.verifier)
            this.realm = simpleJWT.realm()
            validate { credential ->

                if (credential.payload.audience.contains(simpleJWT.audience())) {
                    val subject = credential.payload.subject
                    val jwtDecode = jwtDecode(subject)
                    buildUserPrincipal(jwtDecode)
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
