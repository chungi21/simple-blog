package com.example.simple_blog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.TimeUnit

class JwtManager(
    accessTokenExpireSecond : Long = 300
) {

    private val log = KotlinLogging.logger {}

    private val secretKey : String = "mySecretKey"
    private val claimEmail = "email"
    private val claimPassword = "password"
    private val claimPrincipal = "principal"
    private val accessTokenExpireSecond:Long = accessTokenExpireSecond
    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "
    private val jwtSubject = "my-token"

    fun generateAccessToken(principal: String) : String {

        val expireDate = Date(System.nanoTime() + TimeUnit.SECONDS.toMillis(accessTokenExpireSecond))

        return JWT.create()
            .withSubject(jwtSubject)
            .withExpiresAt(expireDate)
            .withClaim(claimPrincipal, principal)
            .sign(Algorithm.HMAC512(secretKey))
    }

    fun getMemberEmail(token : String) : String? {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token)
            .getClaim(claimEmail).asString()
    }

    fun validatedJwt(accessToken : String) : DecodedJWT {
        try {
            val verifier : JWTVerifier = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
            val jwt : DecodedJWT = verifier.verify(accessToken)
            return jwt
        } catch (exception : JWTVerificationException) {
            log.error { "error : $exception.sta" }
            throw RuntimeException("Invalid jwt")
        }
    }


}