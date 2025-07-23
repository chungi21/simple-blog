package com.example.simple_blog.domain.post

import com.example.simple_blog.domain.AuditingEntity
import com.example.simple_blog.domain.member.Member
import jakarta.persistence.*

@Entity
@Table(name="Post")
class Post(title:String, content:String, member: Member) : AuditingEntity() {

    @Column(name = "title", nullable = false)
    var title:String = title
        protected set

    @Column(name = "content")
    var content:String = content
        protected set

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Member::class)
    @JoinColumn(name = "member_id", nullable = false)
    var member : Member = member
        protected set

    override fun toString() : String{
        return "Post(id=$id, title='$title', content='$content', member=$member)"
    }

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }

}

fun Post.toDTO() : PostRes {
    return PostRes(
        id = this.id!!,
        title = this.title,
        content = this.content,
        member = this.member.toDTO()
    )
}

