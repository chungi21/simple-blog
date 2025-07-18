package com.example.simple_blog.domain.member

import com.example.simple_blog.config.BeanAccessor
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime


data class MemberSaveReq(

    @field:NotNull(message = "require email")
    val email : String?,

    @JsonProperty("password")
    val rawpassword : String?,
    val role : Role?
){
    fun toEntity() : Member{
        return Member(
            email = this.email ?: "",
            password = encodeRawPassword() ?: "",
            role = this.role ?: Role.USER
        )
    }

    private fun encodeRawPassword() : String{
        return BeanAccessor.getBean(PasswordEncoder::class)
            .encode(this.rawpassword)
    }

}

data class MemberRes(
    var id : Long,
    val email : String,
    val password : String,
    val role : Role,
    val createdAt: LocalDateTime,
    val updateAt: LocalDateTime
)