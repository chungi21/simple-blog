package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.member.MemberUpdateReq
import com.example.simple_blog.service.MemberService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/members")
@RestController
class MemberController(private val memberService: MemberService) {

    // 전체 회원 리스트
    @GetMapping("")
    fun findAll(@PageableDefault(size = 10) pageable: Pageable) : CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find All Members", memberService.findAll(pageable))
    }

    // 회원 조회(기준 이메일로 조회, 회원블로그 상단 배너에 사용)
    @GetMapping("/{email}")
    fun searchNicknameFindByEmail(@PathVariable email: String): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "Nickname by email ", memberService.findByEmail(email))
    }

    // 최근 가입한 10명 보여주기(메인에 사용)
    @GetMapping("/recent")
    fun findRecentMembers(): CmResDTO<*> {
        val pageable: Pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        return CmResDTO(HttpStatus.OK, "Recent 10 members", memberService.findAll(pageable))
    }

    // 회원 삭제
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "delete Member by id", memberService.deleteMember(id))
    }

    // 회원 정보 수정
    @PutMapping("/me")
    fun updateMyInfo(
        @AuthenticationPrincipal user: PrincipalDetails,
        @RequestBody dto: MemberUpdateReq
    ): CmResDTO<Any> {
        val updatedMember = memberService.update(user.member.id!!, dto)
        return CmResDTO(HttpStatus.OK, "member updated", updatedMember.toDTO())
    }


    // 회원 정보 수정 Form
    @GetMapping("/me")
    fun getCurrentMember(@AuthenticationPrincipal principal: PrincipalDetails?): CmResDTO<out Any?> {
        return if (principal != null) {
            val member = principal.member
            val memberInfo = member.toDTO()
            CmResDTO(HttpStatus.OK, "now login member info", memberInfo)
        } else {
            CmResDTO(HttpStatus.UNAUTHORIZED, "no login member info ", null)
        }
    }

}