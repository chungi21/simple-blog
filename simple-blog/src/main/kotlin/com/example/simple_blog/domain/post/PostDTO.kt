package com.example.simple_blog.domain.post

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRes

data class PostSaveReq(
    val title : String,
    val content : String,
    val memberId : Long
)

fun PostSaveReq.toEntity() : Post {

    return Post(
        title = this.title,
        content = this.content,
        member = Member.createFakeMember(this.memberId)
    )
}

data class PostRes(
    val id : Long,
    val title : String,
    val content : String,
    val member : MemberRes
)

