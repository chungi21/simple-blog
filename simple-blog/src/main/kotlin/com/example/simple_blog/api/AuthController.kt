package com.example.simple_blog.api

import com.example.simple_blog.domain.member.MemberSaveReq
import com.example.simple_blog.service.AuthService
import com.example.simple_blog.util.value.CmResDTO
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.simple_blog.exception.BusinessException
import com.example.simple_blog.exception.ErrorCode

@RequestMapping("/auth")
@RestController
class AuthController(
    private val authService: AuthService
) {
    val log = KotlinLogging.logger {}

    // 회원가입
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody dto : MemberSaveReq): CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "save Member", authService.saveMember(dto))
    }

    // JWT Token 재발급(Cookie에 refreshCookie를 가지고 있을 때만)
    @PostMapping("/refresh")
    fun refreshToken(
        @CookieValue(name = "refreshCookie", required = false) refreshToken: String?
    ): ResponseEntity<CmResDTO<Any>> {

        if (refreshToken.isNullOrBlank()) {
            throw BusinessException(ErrorCode.INVALID_REFRESH_TOKEN)
        }

        val newAccessToken = authService.reIssueAccessToken(refreshToken)

        return ResponseEntity.ok(
            CmResDTO(HttpStatus.OK, "AccessToken reissue success", mapOf("accessToken" to newAccessToken))
        )
    }

}