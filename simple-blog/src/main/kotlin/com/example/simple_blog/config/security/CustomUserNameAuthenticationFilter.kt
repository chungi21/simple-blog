package com.example.simple_blog.config.security

import com.example.simple_blog.domain.member.MemberSaveReq
import com.example.simple_blog.util.value.func.responseData
import com.example.simple_blog.util.value.value.CmResDTO
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomUserNameAuthenticationFilter(
	private val om : ObjectMapper
) : UsernamePasswordAuthenticationFilter() {

	private val log = KotlinLogging.logger {}
	private val jwtManager = JwtManager()

	override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
		log.info {"login 요청 옴"}

		lateinit var loginDto : MemberSaveReq

		try{
			loginDto = om.readValue(request?.inputStream, MemberSaveReq::class.java)
			log.info { "login Dto : $loginDto" }
		}catch (e:Exception){
			log.error { "loginFilter : 로그인 요청 Dto 생성 중 실패. $e" }
		}

		val autheticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)

		return this.authenticationManager.authenticate(autheticationToken)
	}

	override fun successfulAuthentication(
		request: HttpServletRequest,
		response: HttpServletResponse,
		chain: FilterChain,
		authResult: Authentication
	) {

		log.info {"로그인 완료! JWT 토큰 만들어서 response."}
		val principalDetails = authResult?.principal as PrincipalDetails
		val jwtToken = jwtManager.generateAccessToken(principalDetails)
		response?.addHeader(jwtManager.jwtHeader,"Bearer "+jwtToken)

		val jsonResult = om.writeValueAsString(CmResDTO(HttpStatus.OK, "login sucess", principalDetails.member))
		responseData(response, jsonResult)

	}

}

