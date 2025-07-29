package com.example.simple_blog.domain.member

import com.example.simple_blog.config.BeanAccessor
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime

// 회원가입용
data class MemberSaveReq(

    @field:NotNull(message = "require email")
    val email : String,

    @field:NotNull(message = "require nickname")
    val nickname : String,

    @JsonProperty("password")
    val rawpassword : String,
    val role : Role?
){
    fun toEntity() : Member{
        return Member(
            email = this.email ?: "",
            password = encodeRawPassword() ?: "",
            nickname = this.nickname,
            role = this.role ?: Role.USER
        )
    }

    private fun encodeRawPassword() : String{
        return BeanAccessor.getBean(PasswordEncoder::class)
            .encode(this.rawpassword)
    }
}

// 로그인용
data class LoginReq(
    val email: String,
    @JsonProperty("password")
    val password: String
)

// 회원 정보 수정용
data class MemberUpdateReq(
    val nickname: String,
    @JsonProperty("password")
    val rawpassword: String = ""
)


data class MemberRes(
    var id : Long,
    val email : String,
    val password : String,
    val nickname : String,
    val role : Role,
    val createdAt: LocalDateTime,
    val updateAt: LocalDateTime
)