package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.member.MemberRes
import com.example.simple_blog.domain.member.MemberSaveReq
import com.example.simple_blog.domain.member.MemberUpdateReq
import com.example.simple_blog.service.MemberService
import com.example.simple_blog.util.value.CmResDTO
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class MemberController(private val memberService: MemberService) {

    // 전체 회원 리스트
    @GetMapping("/members")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable) : CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find All Members", memberService.findAll(pageable))
    }

    // 최근 가입한 10명 보여주기(메인에 사용)
    @GetMapping("/members/recent")
    fun findRecentMembers(): CmResDTO<*> {
        val pageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        return CmResDTO(HttpStatus.OK, "Recent 10 members", memberService.findAll(pageable))
    }

    // 회원 조회(id로 조회)
    @GetMapping("/member/{id}")
    fun findById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "find Member by id", memberService.findMemberById(id))
    }

    // 회원 삭제
    @DeleteMapping("/member/{id}")
    fun deleteById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "delete Member by id", memberService.deleteMember(id))
    }

    // 회원 가입
    @PostMapping("/member/join")
    fun signUp(@RequestBody @Valid dto: MemberSaveReq): CmResDTO<Any> {
        val savedMember = memberService.join(dto)
        return CmResDTO(HttpStatus.CREATED, "member join", savedMember.toDTO())
    }

    // 회원 가입 Form
    @GetMapping("/member/join")
    fun joinForm(): CmResDTO<Any> {
        val defaultValues = mapOf("defaultRole" to "USER")
        return CmResDTO(HttpStatus.OK, "join form", defaultValues)
    }

    // 회원 정보 수정
    @PutMapping("/member/me")
    fun updateMyInfo(
        @AuthenticationPrincipal user: PrincipalDetails,
        @RequestBody dto: MemberUpdateReq
    ): CmResDTO<Any> {
        val updatedMember = memberService.update(user.member.id!!, dto)
        return CmResDTO(HttpStatus.OK, "member updated", updatedMember.toDTO())
    }


    // 회원 정보 수정 Form
    @GetMapping("/members/me")
    fun getCurrentMember(@AuthenticationPrincipal principal: PrincipalDetails?): CmResDTO<out Any?> {
        return if (principal != null) {
            val member = principal.member
            val memberInfo = member.toDTO()
            CmResDTO(HttpStatus.OK, "now login info", memberInfo)
        } else {
            CmResDTO(HttpStatus.UNAUTHORIZED, "login no info ", null)
        }
    }

    // 회원블로그 상단 배너에 사용
    @GetMapping("/member/email/{email}")
    fun searchNicknameFindByEmail(@PathVariable email: String): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "Nickname by email ", memberService.findByEmail(email))
    }

}