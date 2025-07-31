package com.example.simple_blog.domain.comment

import com.example.simple_blog.domain.AuditingEntity
import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.post.Post
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "Comment")
class Comment(
    content: String,
    post: Post,
    member: Member
) : AuditingEntity() {

    @Column(name = "content", nullable = false)
    var content: String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    var post: Post = post
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member = member
        protected set

    fun updateContent(newContent: String) {
        this.content = newContent
    }
}
