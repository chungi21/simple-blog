package com.example.simple_blog.domain.member

data class MemberSaveReq(
    val email : String,
    val password : String,
    val role : Role
)

fun MemberSaveReq.toEntity() : Member{
    return Member(
        email = this.email,
        password = this.password,
        role = this.role
    )
}

data class MemberRes(
    var id : Long,
    val email : String,
    val password : String,
    val role : Role
)