package com.example.simple_blog.service

import com.example.simple_blog.domain.member.*
import com.example.simple_blog.exception.MemberNotFoundException
import com.example.simple_blog.exception.MemberAlreadyExistsException
import com.example.simple_blog.exception.NicknameAlreadyExistsException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional(readOnly = true)
    fun findAll(pageable : Pageable) : Page<MemberRes> =
        memberRepository.findMembers(pageable).map{
            it.toDTO()
        }

    @Transactional
    fun deleteMember(id : Long){
        return memberRepository.deleteById(id)
    }

    @Transactional
    fun update(id: Long, dto: MemberUpdateReq): Member {
        val member = memberRepository.findById(id).orElseThrow {
            MemberNotFoundException(id.toString())
        }

        // 이메일 변경 여부 체크(이메일은 회원 정보 수정시 변경 불가)
        if(dto.email != member.email){
            throw IllegalArgumentException("Email cannot be changed.")
        }

        // 닉네임 중복 체크
        val existing = memberRepository.findMemberByNicknameOrNull(dto.nickname)
        if (existing != null && existing.id != id) {
            throw NicknameAlreadyExistsException(dto.nickname)
        }

        member.changeNickname(dto.nickname)

        if (dto.rawpassword.isNotBlank()) {
            member.changePassword(passwordEncoder.encode(dto.rawpassword))
        }

        return member
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): MemberNicknameRes {
        return memberRepository.findMemberByEmailOrNull(email)
            ?.toNicknameDTO()
            ?: throw MemberNotFoundException(email)
    }

    @Transactional(readOnly = true)
    fun isEmailExists(email: String): Member? {
        return memberRepository.findMemberByEmailOrNull(email)
    }

    @Transactional(readOnly = true)
    fun isNicknameExists(nickname: String): Member? {
        return memberRepository.findMemberByNicknameOrNull(nickname)
    }

}