package com.example.simple_blog.api

import com.example.simple_blog.domain.member.MemberSaveReq
import com.example.simple_blog.service.MemberService
import com.example.simple_blog.util.value.value.CmResDTO
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api")
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

    @GetMapping("/member/{id}")
    fun findById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "find Member by id", memberService.findMemberById(id))
    }

    @DeleteMapping("/member/{id}")
    fun deleteById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "delete Member by id", memberService.deleteMember(id))
    }

    @PostMapping("/member")
    fun save(@Valid @RequestBody dto : MemberSaveReq): CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "save Member", memberService.saveMember(dto))
    }


}