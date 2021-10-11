package com.github.blanexie.nexusj.support

import cn.hutool.core.date.DateUtil
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.symmetric.AES
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.github.blanexie.nexusj.setting
import io.ktor.application.*
import java.util.*




val jwtAesKey = setting.get("jwt.aesKey")!!
val issuer = setting.get("jwt.domain")!!
val audience = setting.get("jwt.audience")!!
val realm = setting.get("jwt.realm")!!
val secret = setting.get("jwt.secret")!!

var algorithm: Algorithm =  Algorithm.HMAC256(secret)

val aes: AES = SecureUtil.aes(jwtAesKey.toByteArray())

open class SimpleJWT {

    val verifier: JWTVerifier = JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun issuer():String{
        return  issuer
    }
    fun realm():String{
        return  realm
    }
    fun audience():String{
        return  audience
    }
    fun secret():String{
        return  secret
    }

}


fun Application.jwtSign(subject: String): String {
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