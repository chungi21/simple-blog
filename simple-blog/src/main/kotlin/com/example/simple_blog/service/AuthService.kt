package com.example.simple_blog.service

import com.example.simple_blog.config.security.JwtManager
import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.config.security.TokenValidResult
import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.member.MemberSaveReq
import com.example.simple_blog.exception.BusinessException
import com.example.simple_blog.exception.ErrorCode
import com.example.simple_blog.exception.MemberAlreadyExistsException
import com.example.simple_blog.exception.NicknameAlreadyExistsException
import com.example.simple_blog.exception.InvalidPasswordLengthException
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
	private val memberRepository: MemberRepository,
	private val jwtManager: JwtManager
) : UserDetailsService {

	val log = KotlinLogging.logger {}

	override fun loadUserByUsername(email: String): UserDetails {
		log.info { "loadUserByUsername 호출" }
		val member = memberRepository.findMemberByEmailOrNull(email)
			?: throw UsernameNotFoundException("email does not exist : $email")
		return PrincipalDetails(member)
	}

	@Transactional
	fun saveMember(dto : MemberSaveReq): Member {
		// 유효성 검사
		// 이메일 값이 있는지 체크
		if (dto.email.isNullOrBlank()) {
			throw IllegalArgumentException("required input email")
		}

		// 닉네임 값이 있는지 체크
		if (dto.nickname.isNullOrBlank()) {
			throw IllegalArgumentException("required input nickname")
		}
		
		// 비밀번호 값이 있는지 체크
		if (dto.rawpassword.isNullOrBlank()) {
			throw IllegalArgumentException("required input password")
		}

		// 이메일 중복 체크
		val checkEmail = memberRepository.findMemberByEmailOrNull(dto.email)
		if (checkEmail != null) {
			throw MemberAlreadyExistsException(dto.email)
		}

		// 닉네임 중복 체크
		val checkNickname = memberRepository.findMemberByNicknameOrNull(dto.nickname)
		if (checkNickname != null) {
			throw NicknameAlreadyExistsException(dto.nickname)
		}

		// 이메일 길이 확인(4자리 이상)
		if(dto.rawpassword.length<4){
			throw InvalidPasswordLengthException(dto.rawpassword.length)
		}

		val member = dto.toEntity()
		return memberRepository.save(member)
	}

	fun reIssueAccessToken(refreshToken: String): String {
		// refreshToken 유효성 검사
		val result = jwtManager.validRefreshToken(refreshToken)

		if (result is TokenValidResult.Failure) {
			throw BusinessException(ErrorCode.INVALID_REFRESH_TOKEN)
		}

		// 회원 정보 추출
		val memberTokenInfo = jwtManager.getPrincipalStringByRefreshToken(refreshToken)

		// accessToken 재발급
		return jwtManager.generateAccessToken(memberTokenInfo)
	}


}