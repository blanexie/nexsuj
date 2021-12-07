package com.github.blanexie.nexusj.support

import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.ZipUtil
import cn.hutool.crypto.digest.DigestAlgorithm
import cn.hutool.crypto.digest.Digester
import cn.hutool.crypto.symmetric.AES
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import java.util.*

val issuer = setting["jwt.domain"]!!
val audience = setting["jwt.audience"]!!
val realm = setting["jwt.realm"]!!

val jwtAesKey = setting["jwt.aesKey"]!!
val secret = setting["jwt.secret"]!!
val algorithm: Algorithm = Algorithm.HMAC256(secret)
val aes: AES = AES(Digester(DigestAlgorithm.SHA256).digest(jwtAesKey))
val verifier: JWTVerifier = JWT.require(algorithm).withAudience(audience).withIssuer(issuer).build()

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
    val encrypt = aes.decrypt(subject)
    val unGzip = ZipUtil.unGzip(encrypt)
    return String(unGzip)
}

fun Application.jwtEncode(subject: String): String {
    val gzip = ZipUtil.gzip(subject.toByteArray())
    return aes.encryptBase64(gzip)
}
