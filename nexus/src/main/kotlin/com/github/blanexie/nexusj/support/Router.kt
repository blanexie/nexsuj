package com.github.blanexie.nexusj.support

import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.setting.Setting
import com.github.blanexie.nexusj.controller.auth
import com.github.blanexie.nexusj.controller.notAuth
import com.github.blanexie.nexusj.controller.tracker
import com.github.blanexie.nexusj.table.UserDO
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.routing.*
import java.text.DateFormat
import java.time.LocalDateTime

val setting = Setting(ClassPathResource(System.getProperty("properties.path") ?: "app.properties").path)

/**
 * 从token 恢复用户的信息
 */
fun buildUserPrincipal(jwtDecode: String): UserPrincipal {
    val userMap = gson.fromJson<Map<String, *>>(jwtDecode, Map::class.java)
    val userDO = UserDO()
    userMap.forEach { (t, u) ->
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
    //json序列化设置
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            registerTypeAdapter(LocalDateTime::class.java, JsonDeserializerImpl())
            registerTypeAdapter(LocalDateTime::class.java, JsonSerializerImpl())
            setPrettyPrinting()
        }
    }
    //跨域设置
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        anyHost()
    }
    install(Authentication) {
        jwt {
            verifier(verifier)
            this.realm = realm
            validate { credential ->
                if (credential.payload.audience.contains(audience)) {
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
        route(("/announce")) {
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
