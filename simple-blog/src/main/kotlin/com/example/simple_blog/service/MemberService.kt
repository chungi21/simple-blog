package com.example.simple_blog.service

import com.example.simple_blog.domain.member.*
import com.example.simple_blog.exception.MemberNotFoundException
import com.example.simple_blog.exception.MemberAlreadyExistsException
import com.example.simple_blog.exception.NicknameAlreadyExistsException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {

    @Transactional(readOnly = true)
    fun findAll(pageable : Pageable) : Page<MemberRes> =
        memberRepository.findMembers(pageable).map{
            it.toDTO()
        }

    @Transactional
    fun deleteMember(id : Long){
        return memberRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findMemberById(id : Long) : MemberRes{
        return memberRepository.findById(id)
            .orElseThrow{
                throw MemberNotFoundException(id.toString())
            }.toDTO()
    }

    @Transactional
    fun join(dto: MemberSaveReq): Member {
        val checkEmail = memberRepository.findMemberByEmailOrNull(dto.email)
        if (checkEmail != null) {
            throw MemberAlreadyExistsException(dto.email)
        }

        val checkNickname = memberRepository.findMemberByNicknameOrNull(dto.nickname)
        if (checkNickname != null) {
            throw NicknameAlreadyExistsException(dto.nickname)
        }

        val member = dto.toEntity()
        return memberRepository.save(member)
    }

}