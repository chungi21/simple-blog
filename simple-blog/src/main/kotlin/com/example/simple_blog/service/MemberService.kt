package com.example.simple_blog.service

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.member.MemberRes
import com.example.simple_blog.domain.member.toDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {

    /*@Transactional
    fun findAll() : MutableList<Member> = memberRepository.findAll()*/

    @Transactional(readOnly = true)
    fun findAll(pageable: Pageable) : Page<MemberRes> =
        memberRepository.findMembers(pageable).map{
            it.toDTO()
        }

}