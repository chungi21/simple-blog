package com.example.simple_blog.domain.comment

import com.example.simple_blog.domain.AuditingEntity
import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.post.Post
import jakarta.persistence.*

@Entity
@Table(name = "Comment")
class Comment(title : String, content : String, post : Post) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content : String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    var post : Post = post
        protected set

}