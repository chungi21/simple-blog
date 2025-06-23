package com.example.simple_blog.service

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.member.MemberRes
import com.example.simple_blog.domain.member.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {

    /*@Transactional
    fun findAll() : MutableList<Member> = memberRepository.findAll()*/

    @Transactional(readOnly = true)
    fun findAll() : List<MemberRes> =
        memberRepository.findAll().map{
            it.toDTO()
        }

}