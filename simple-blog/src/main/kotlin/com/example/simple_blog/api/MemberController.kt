package com.example.simple_blog.api

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.service.MemberService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(private val memberService: MemberService) {
/*
    @GetMapping("/members")
    fun findAll() : MutableList<Member>{
        return memberService.findAll()
    }
*/
    @GetMapping("/members")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable) : CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find All Members", memberService.findAll(pageable))
    }

}