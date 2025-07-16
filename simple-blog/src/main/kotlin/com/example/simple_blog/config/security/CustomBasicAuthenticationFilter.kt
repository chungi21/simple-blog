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
		val header = request.getHeader(jwtManager.authorizationHeader)
		if (header == null || !header.startsWith(jwtManager.jwtHeader)) {
			chain.doFilter(request, response)
			return
		}

		val accessToken = header.replace(jwtManager.jwtHeader, "")
		val accessTokenResult = jwtManager.validAccessToken(accessToken)

		val principalDetails: PrincipalDetails = when (accessTokenResult) {

			is TokenValidResult.Success -> {
				// AccessToken 유효 → 이메일로 복원 (AccessToken엔 이메일만 있으므로)
				val email = jwtManager.getMemberEmail(accessToken)
					?: return chain.doFilter(request, response) // 실패시 필터 통과
				val member = memberRepository.findMemberByEmail(email)
					?: return chain.doFilter(request, response)
				PrincipalDetails(member)
			}

			is TokenValidResult.Failure -> {
				if (accessTokenResult.exception is TokenExpiredException) {
					log.info { "AccessToken 만료 → RefreshToken 검사 및 재발급 시도" }

					try {
						val refreshToken = CookieProvider.getCookie(request, "refreshCookie").orElseThrow()
						val refreshTokenResult = jwtManager.validRefreshToken(refreshToken)

						if (refreshTokenResult is TokenValidResult.Failure) {
							log.error { "RefreshToken도 유효하지 않음!" }
							return chain.doFilter(request, response)
						}

						// 여기선 refreshToken에는 principal 전체 JSON이 있으므로
						val principalJson = jwtManager.getPrincipalStringByRefreshToken(refreshToken)

						val details = om.readValue(principalJson, PrincipalDetails::class.java)

						// AccessToken 재발급 후 헤더에 추가
						val newAccessToken = jwtManager.generateAccessToken(details.member.email)
						response.addHeader(jwtManager.authorizationHeader, jwtManager.jwtHeader + newAccessToken)

						details
					} catch (e: Exception) {
						log.error { "RefreshToken 처리 중 예외 발생: ${e.message}" }
						return chain.doFilter(request, response)
					}


				} else {
					log.error { "AccessToken 유효성 검사 실패: ${accessTokenResult.exception.message}" }
					return chain.doFilter(request, response)
				}
			}
		}

		// SecurityContext에 인증 저장
		val authentication = UsernamePasswordAuthenticationToken(
			principalDetails,
			null,
			principalDetails.authorities
		)
		SecurityContextHolder.getContext().authentication = authentication

		chain.doFilter(request, response)
	}

}

