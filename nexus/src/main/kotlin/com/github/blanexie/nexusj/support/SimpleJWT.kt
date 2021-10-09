package com.github.blanexie.nexusj.support

import cn.hutool.core.date.DateUtil
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.symmetric.AES
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import java.util.*


var algorithm: Algorithm? = null

const val jwtAesKey = "XxIkq9eUmfAsQAFqwOsjEE5gtvfYKJkErjZRBMtV2ipkKGZ0LGy4vymMMp9nSi3K"
val aes: AES = SecureUtil.aes(jwtAesKey.toByteArray())


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


fun Application.jwtSign(subject: String): String {
    if (algorithm == null) {
        val secret = environment.config.property("jwt.secret").getString()
        algorithm = Algorithm.HMAC256(secret)
    }
    val issuer = environment.config.property("jwt.domain").getString()
    val audience = environment.config.property("jwt.audience").getString()

    val expiredDate = DateUtil.offsetDay(Date(), 7)
    return JWT.create()
        .withSubject(jwtEncode(subject))
        .withExpiresAt(expiredDate)
        .withAudience(audience)
        .withIssuer(issuer)
        .sign(algorithm)
}

fun Application.jwtDecode(subject: String): String {
    return aes.decryptStr(subject)
}

fun Application.jwtEncode(subject: String): String {
    return aes.encryptHex(subject)
}