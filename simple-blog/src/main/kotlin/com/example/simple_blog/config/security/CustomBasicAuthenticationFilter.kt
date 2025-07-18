package com.example.simple_blog.config.security

import com.auth0.jwt.exceptions.TokenExpiredException
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.util.CookieProvider
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter


class CustomBasicAuthenticationFilter(
	private val memberRepository: MemberRepository,
	private val om: ObjectMapper,
	authenticationManager: AuthenticationManager
) : BasicAuthenticationFilter(authenticationManager) {

	private val log = KotlinLogging.logger {}
	private val jwtManager = JwtManager()

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		chain: FilterChain
	) {
		log.info { "권한이나 인증이 필요한 요청이 들어옴" }

		val uri = request.requestURI
		val skipUrls = listOf(
			"/login", "/logout", "/auth/login", "/auth/member", "/api/token/refresh"
		)

		// ✅ 1. 인증이 필요 없는 요청이라면 필터 통과
		if (skipUrls.any { uri.startsWith(it) }) {
			log.info { "인증이 필요 없는 요청 $uri → 필터 통과" }
			chain.doFilter(request, response)
			return
		}

		// ✅ 2. Authorization 헤더 체크
		val header = request.getHeader(jwtManager.authorizationHeader)
		if (header == null || !header.startsWith(jwtManager.jwtHeader)) {
			log.warn { "Authorization 헤더가 없거나 형식이 잘못됨" }
			chain.doFilter(request, response)
			return
		}

		// ✅ 3. AccessToken 검증
		val accessToken = header.replace(jwtManager.jwtHeader, "")
		val accessTokenResult: TokenValidResult = jwtManager.validAccessToken(accessToken)
		if (accessTokenResult is TokenValidResult.Failure) {
			if (accessTokenResult.exception is TokenExpiredException) {
				log.info { "AccessToken 만료됨: ${accessTokenResult.exception}" }
			} else {
				log.warn { "AccessToken 오류: ${accessTokenResult.exception.stackTraceToString()}" }
			}
			chain.doFilter(request, response)
			return
		}

		// ✅ 4. 이메일 추출 및 사용자 조회
		val email = jwtManager.getMemberEmail(accessToken)
		if (email == null) {
			log.error { "JWT에서 이메일 claim 추출 실패" }
			chain.doFilter(request, response)
			return
		}

		val member = memberRepository.findMemberByEmail(email)
		if (member == null) {
			log.error { "이메일에 해당하는 사용자 없음: $email" }
			chain.doFilter(request, response)
			return
		}

		// ✅ 5. 인증객체 생성 및 SecurityContext에 저장
		val principalDetails = PrincipalDetails(member)
		val authentication = UsernamePasswordAuthenticationToken(
			principalDetails,
			null,
			principalDetails.authorities
		)
		SecurityContextHolder.getContext().authentication = authentication

		chain.doFilter(request, response)
	}

}
