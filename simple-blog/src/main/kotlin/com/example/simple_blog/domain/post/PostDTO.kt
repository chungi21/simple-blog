package com.example.simple_blog.domain.post

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRes
import jakarta.validation.constraints.NotNull

data class PostSaveReq(
    @field:NotNull(message = "require title")
    val title : String,
    val content : String,
    @field:NotNull(message = "require memberId")
    val memberId : Long?
)

data class PostUpdateReq(
    val title: String,
    val content: String
)

data class PostEditRes(
    val id: Long,
    val title: String,
    val content: String
)

fun PostSaveReq.toEntity() : Post {

    return Post(
        title = this.title ?: "",
        content = this.content ?: "",
        member = Member.createFakeMember(this.memberId!!)
    )
}

data class PostRes(
    val id : Long,
    val title : String,
    val content : String,
    val member : MemberRes
)