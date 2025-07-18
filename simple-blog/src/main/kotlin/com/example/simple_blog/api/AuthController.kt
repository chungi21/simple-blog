package com.example.simple_blog.api

import com.example.simple_blog.domain.member.MemberSaveReq
import com.example.simple_blog.service.AuthService
import com.example.simple_blog.util.value.CmResDTO
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {
    val log = KotlinLogging.logger {}

    @GetMapping("/login")
    fun login(session : HttpSession){
        session.setAttribute("principal","pass")
    }

    @PostMapping("/member")
    fun joinApp(@Valid @RequestBody dto : MemberSaveReq): CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "save Member", authService.saveMember(dto))
    }

}