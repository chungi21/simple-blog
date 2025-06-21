package com.example.simple_blog.api

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.service.MemberService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(private val memberService: MemberService) {

    @GetMapping("/members")
    fun findAll() : MutableList<Member>{
        return memberService.findAll()
    }

}