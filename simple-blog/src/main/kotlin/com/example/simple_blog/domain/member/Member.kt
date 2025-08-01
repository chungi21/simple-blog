package com.example.simple_blog.domain.member

import com.example.simple_blog.domain.AuditingEntity
import jakarta.persistence.*

@Entity
@Table(name="Member")
class Member(email:String, password:String, nickname:String, role:Role) : AuditingEntity() {

    @Column(name = "email", nullable = false)
    var email:String = email
        protected set

    @Column(name = "nickname", nullable = false)
    var nickname:String = nickname
        protected set

    @Column(name = "password")
    var password:String = password
        protected set

    @Enumerated(EnumType.STRING)
    var role:Role = role
        protected set

    fun toDTO() : MemberRes {
        return MemberRes(
            id = this.id!!,
            email =  this.email,
            nickname = this.nickname,
            password = this.password,
            role = this.role,
            createdAt = this.createAt,
            updateAt = this.updateAt
        )
    }

    fun toNicknameDTO(): MemberNicknameRes {
        return MemberNicknameRes(nickname = this.nickname)
    }

    override fun toString() : String {
        return "Member(id=$id, email='$email', password='$password', role='$role', createdAt='$createAt')"
    }

    companion object {
        fun createFakeMember(memberId: Long) : Member {
            val member = Member("","", "", Role.USER)
            member.id = memberId
            return member
        }
    }

    fun changeNickname(newNickname: String) {
        this.nickname = newNickname
    }

    fun changePassword(encodedPassword: String) {
        this.password = encodedPassword
    }

}

enum class Role {
    USER, ADMIN
}