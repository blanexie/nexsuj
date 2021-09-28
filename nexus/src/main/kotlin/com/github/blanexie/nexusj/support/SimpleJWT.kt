package com.github.blanexie.nexusj.support

import cn.hutool.core.date.DateUtil
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import java.util.*


var algorithm: Algorithm? = null


open class SimpleJWT(secret: String, issuer: String, audience: String) {
    init {
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(secret)
        }
    }

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
}


fun Application.jwtSign(userId: Int): String {
    if (algorithm == null) {
        val secret = environment.config.property("jwt.secret").getString()
        algorithm = Algorithm.HMAC256(secret)
    }
    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()

    val expiredDate = DateUtil.offsetDay(Date(), 2)
    return JWT.create()
        .withSubject(userId.toString())
        .withExpiresAt(expiredDate)
        .withAudience(audience)
        .withIssuer(issuer)
        .sign(algorithm)
}