package com.example.simple_blog.service

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberService(private val memberRepository: MemberRepository) {

    @Transactional
    fun findAll() : MutableList<Member> = memberRepository.findAll()

}