package com.example.simple_blog.service

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.member.MemberRes
import com.example.simple_blog.domain.member.MemberSaveReq
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
	private val memberRepository: MemberRepository
) : UserDetailsService {

	val log = KotlinLogging.logger {}

	override fun loadUserByUsername(email: String): UserDetails {
		log.info { "loadUserByUsername 호출" }
		val member = memberRepository.findMemberByEmailOrNull(email)
			?: throw UsernameNotFoundException("email does not exist : $email")
		return PrincipalDetails(member)
	}

	@Transactional
	fun saveMember(dto : MemberSaveReq): MemberRes {
		return memberRepository.save(dto.toEntity()).toDTO()
	}

}