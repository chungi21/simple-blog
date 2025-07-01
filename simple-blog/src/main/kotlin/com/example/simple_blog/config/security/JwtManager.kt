package com.example.simple_blog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.security.Principal
import java.util.*

class JwtManager {

    private val log = KotlinLogging.logger {}

    fun generateAccessToken(principal: PrincipalDetails){

        JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(Date(System.nanoTime() + 1000*60*60))
            .withClaim("email", principal.username)
            .withClaim("password", principal.password)
            .sign(Algorithm.HMAC512("asdfasdf"))

    }

}