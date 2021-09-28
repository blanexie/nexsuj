package com.github.blanexie.nexusj

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.github.blanexie.dao.UserDO
import com.github.blanexie.dao.database
import com.github.blanexie.dao.userDO
import com.github.blanexie.nexusj.controller.auth
import com.github.blanexie.nexusj.controller.notAuth
import com.github.blanexie.nexusj.support.SimpleJWT
import com.github.blanexie.nexusj.support.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import org.ktorm.dsl.eq
import org.ktorm.entity.first
import java.text.DateFormat

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.nexus(testing: Boolean = true) {

    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val realm = environment.config.property("jwt.realm").getString()
    val secret = environment.config.property("jwt.secret").getString()

    val simpleJWT = SimpleJWT(secret, issuer, audience)

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
            this.realm = realm
            validate { credential ->
                if (credential.payload.audience.contains(audience)) {
                    val userDO = database.userDO.first { it.id eq credential.payload.subject.toInt() }
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
