package com.example.simple_blog.config.security

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.Role
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class JwtManagerTest {

    private val log = KotlinLogging.logger {}

    private val jwtManager = JwtManager()

    @Test
    fun bcryptEncodeTest(){
        val encoder = BCryptPasswordEncoder()
        val encpasswrod = encoder.encode("1234")
        log.info { encpasswrod }
    }

    @Test
    fun generateJwtToken() {

        val jwtManager = JwtManager(accessTokenExpireSecond = 60)

        val details = PrincipalDetails(Member.createFakeMember(1))


    }
}
