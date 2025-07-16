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

		val header = request.getHeader(jwtManager.authorizationHeader)
		if (header == null || !header.startsWith(jwtManager.jwtHeader)) {
			log.warn { "Authorization 헤더가 없거나 형식이 잘못됨" }
			chain.doFilter(request, response)
			return
		}

		val accessToken = header.replace(jwtManager.jwtHeader, "")
		val accessTokenResult: TokenValidResult = jwtManager.validAccessToken(accessToken)
		if (accessTokenResult is TokenValidResult.Failure) {
			if (accessTokenResult.exception is TokenExpiredException) {

				// accessToken 유효시간이나 로그인이 제대로 되지않는다면 accessToken이 제대로 없다.
				log.info { "TokenExpiredException ==> ${accessTokenResult.exception.javaClass}" }

/*
				val refreshToken = CookieProvider.getCookie(request, "refreshCookie").orElseThrow()
				val refreshTokenResult = jwtManager.validRefreshToken(refreshToken)
				if (refreshTokenResult is TokenValidResult.Failure) {
					throw RuntimeException("refreshToken invalid")
				}


				val principalString = jwtManager.getPrincipalStringByRefreshToken(refreshToken)
				val details = om.readValue(principalString, PrincipalDetails::class.java)

				val accessToken = jwtManager.generateAccessToken(om.writeValueAsString(details))
				response?.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + accessToken)


				val authentication = UsernamePasswordAuthenticationToken(
					details,
					null,  // 비밀번호 null로 넣고,
					details.authorities  // 권한을 반드시 넘겨야 인가 성공
				)

				SecurityContextHolder.getContext().authentication = authentication

				chain.doFilter(request, response)

				return
*/


			} else {
				log.info { "else Exception" + accessTokenResult.exception.stackTraceToString() }
			}
		}

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

		val principalDetails = PrincipalDetails(member)

		val authentication = UsernamePasswordAuthenticationToken(
			principalDetails,
			null,  // 비밀번호 null로 넣고,
			principalDetails.authorities  // 권한을 반드시 넘겨야 인가 성공
		)

		SecurityContextHolder.getContext().authentication = authentication

		chain.doFilter(request, response)
	}
}
/*
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

		val header = request.getHeader(jwtManager.authorizationHeader)
		if (header == null || !header.startsWith(jwtManager.jwtHeader)) {
			log.warn { "Authorization 헤더가 없거나 형식이 잘못됨" }
			chain.doFilter(request, response)
			return
		}

		val accessToken = header.replace(jwtManager.jwtHeader, "")
		val accessTokenResult = jwtManager.validAccessToken(accessToken)

		val principalDetails: PrincipalDetails = when (accessTokenResult) {
			is TokenValidResult.Success -> {
				val principalString = jwtManager.getPrincipalStringByAccessToken(accessToken)
				om.readValue(principalString, PrincipalDetails::class.java)
			}
			is TokenValidResult.Failure -> {
				log.warn { "AccessToken 유효하지 않음. ${accessTokenResult.exception}" }

				if (accessTokenResult.exception is TokenExpiredException) {
					val refreshToken = CookieProvider.getCookie(request, "refreshCookie")
						.orElseThrow { RuntimeException("refreshToken 없음") }

					val refreshTokenResult = jwtManager.validRefreshToken(refreshToken)
					if (refreshTokenResult is TokenValidResult.Failure) {
						throw RuntimeException("refreshToken 유효하지 않음")
					}

					val principalString = jwtManager.getPrincipalStringByRefreshToken(refreshToken)
					val refreshedDetails = om.readValue(principalString, PrincipalDetails::class.java)

					// 🔥 accessToken 재발급 (principal 기반)
					val newAccessToken = jwtManager.generateAccessToken(principalString)
					response.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + newAccessToken)

					refreshedDetails
				} else {
					chain.doFilter(request, response)
					return
				}
			}
		}

		val authentication = UsernamePasswordAuthenticationToken(
			principalDetails,
			null,
			principalDetails.authorities
		)

		SecurityContextHolder.getContext().authentication = authentication
		chain.doFilter(request, response)
	}
}

*/
