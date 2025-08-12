package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.member.MemberRes
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

    // 회원 조회(전체 + 최근)
    @GetMapping("")
    fun findMembers(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC)
        pageable: Pageable,
        @RequestParam(required = false) limit: Int?
    ): CmResDTO<*> {
        val newPageable = if (limit != null) {
            PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))
        } else {
            pageable
        }
        return CmResDTO(HttpStatus.OK, "find members", memberService.findAll(newPageable))
    }

    // 회원 조회(기준 이메일로 조회, 회원블로그 상단 배너에 사용)
    @GetMapping("/{email}")
    fun searchNicknameFindByEmail(@PathVariable email: String): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "Nickname by email ", memberService.findByEmail(email))
    }
    
    // 회원 삭제
    @DeleteMapping("/")
    fun deleteById(@AuthenticationPrincipal user: PrincipalDetails): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "delete Member by id", memberService.delete(user.member.id!!))
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
    fun getCurrentMember(@AuthenticationPrincipal principal: PrincipalDetails): CmResDTO<Any> {
        val member = principal.member
        val memberInfo = member.toDTO()
        return CmResDTO(HttpStatus.OK, "now login member info", memberInfo)
    }

    // email 중복 체크(회원가입에서 사용)
    @GetMapping("/check-email")
    fun checkEmail(@RequestParam email: String): CmResDTO<Any> {
        val exists = memberService.isEmailExists(email)
        if(exists != null){
            return CmResDTO(HttpStatus.OK, "join - email check existence", mapOf("exists" to exists))
        }else{
            return CmResDTO(HttpStatus.OK, "join - email check success", mapOf("exists" to exists))
        }
    }

    // nickname 중복 체크(회원가입, 회원 정보 수정에서 사용)
    @GetMapping("/check-nickname")
    fun checkNickname(@RequestParam nickname: String): CmResDTO<Any> {
        val exists = memberService.isNicknameExists(nickname)
        if(exists!=null){
            return CmResDTO(HttpStatus.OK, "join - nickname check existence", mapOf("exists" to exists))
        }else{
            return CmResDTO(HttpStatus.OK, "join - nickname check success", mapOf("exists" to exists))
        }
    }

}