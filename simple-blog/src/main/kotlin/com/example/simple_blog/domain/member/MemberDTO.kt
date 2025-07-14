package com.example.simple_blog.domain.member

import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime


data class MemberSaveReq(

    @field:NotNull(message = "require email")
    val email : String?,
    val password : String?,
    val role : Role?
)

fun MemberSaveReq.toEntity() : Member{
    return Member(
        email = this.email ?: "",
        password = this.password ?: "",
        role = this.role ?: Role.USER
    )
}

data class MemberRes(
    var id : Long,
    val email : String,
    val password : String,
    val role : Role,
    val createdAt: LocalDateTime,
    val updateAt: LocalDateTime
)