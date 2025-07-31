package com.example.simple_blog.util

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.ResponseCookie
import java.util.*
import java.time.Duration

object CookieProvider {

	private val log = KotlinLogging.logger {}

	fun createNullCookie(cookieName : String) : String {
		TODO()
	}

	fun createCookie(cookieName: String, value: String, maxAgeSeconds: Long): ResponseCookie {
		val isSecure = false // 운영 환경에서는 true로 설정
		return ResponseCookie.from(cookieName, value)
			.httpOnly(true)
			.secure(isSecure)
			.path("/")
			.maxAge(Duration.ofSeconds(maxAgeSeconds))
			.sameSite("Lax") // 또는 Strict/None
			.build()
	}

	fun getCookie(req : HttpServletRequest, cookieName : String): Optional<String> {

		val cookieValue = req.cookies.filter { cookie ->
			cookie.name == cookieName
		}.map { cookie ->
			cookie.value
		}.firstOrNull()

		log.info { " cookieValue ==> $cookieValue " }

		return Optional.ofNullable(cookieValue)

	}

}