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
    accessTokenExpireSecond : Long = 600,
    refreshTokenExpireDay : Long = 7
) {

    private val log = KotlinLogging.logger {}

    private val accessSecretKey : String = "myAccessSecretKey"
    private val refreshSecretKey : String = "myRefreshSecretKey"

    private val claimEmail = "email"
    private val claimPassword = "password"
    private val claimPrincipal = "principal"
    private val accessTokenExpireSecond : Long = accessTokenExpireSecond
    val refreshTokenExpireDay : Long = refreshTokenExpireDay
    val authorizationHeader = "Authorization"
    val jwtHeader = "Bearer "
    private val jwtSubject = "my-token"

    private fun doGenerateToken(
        expireDate : Date,
        principal : String,
        secretKey : String
    ) = JWT.create()
        .withSubject(jwtSubject)
        .withExpiresAt(expireDate)
        .withClaim(claimPrincipal, principal)
        .sign(Algorithm.HMAC512(secretKey))

    fun generateRefreshToken(principal: String): String {
        val expireDate = Date(System.nanoTime() + TimeUnit.DAYS.toMillis(refreshTokenExpireDay))
        return doGenerateToken(expireDate, principal, refreshSecretKey)
    }

/*    fun generateAccessToken(principal: String) : String {
        val expireDate = Date(System.nanoTime() + TimeUnit.SECONDS.toMillis(accessTokenExpireSecond))
        return doGenerateToken(expireDate, principal, accessSecretKey)
    }*/

    fun generateAccessToken(principal: String): String {
        val expireDate = Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(accessTokenExpireSecond))

        return JWT.create()
            .withSubject(jwtSubject)
            .withExpiresAt(expireDate)
            .withClaim(claimEmail, principal) // 이메일 claim 저장
            .sign(Algorithm.HMAC512(accessSecretKey))
    }


    fun getMemberEmail(token : String) : String? {
        return JWT.require(Algorithm.HMAC512(accessSecretKey)).build().verify(token)
            .getClaim(claimEmail).asString()
    }

    fun getPrincipalStringByAccessToken(accessToken : String) : String {
        val decodedJWT = getDecodeJwt(secretKey= accessSecretKey, token = accessToken)
        return decodedJWT.getClaim(claimPrincipal).asString()
    }

    fun getPrincipalStringByRefreshToken(refreshToken : String) : String {
        val decodedJWT = getDecodeJwt(secretKey= refreshSecretKey, token = refreshToken)
        return decodedJWT.getClaim(claimPrincipal).asString()
    }


    private fun getDecodeJwt(secretKey : String, token : String) : DecodedJWT {
        val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(accessSecretKey))
            .build()
        val decodedJWT : DecodedJWT = verifier.verify(token)
        return decodedJWT
    }

    fun validAccessToken(token : String) : TokenValidResult {
        return validatedJwt(token, accessSecretKey)
    }

    fun validRefreshToken(token : String) : TokenValidResult {
        return validatedJwt(token, refreshSecretKey)
    }

    fun validatedJwt(token : String, secretKey : String) : TokenValidResult {
        try {
            getDecodeJwt(secretKey, token)
            return TokenValidResult.Success()
        } catch (exception : JWTVerificationException) {
            log.error { "error : $exception.sta" }
            return TokenValidResult.Failure(exception)
        }
    }

}

sealed class TokenValidResult {
    class Success(val successValue : Boolean = true) : TokenValidResult()
    class Failure(val exception : JWTVerificationException) : TokenValidResult()
}

















