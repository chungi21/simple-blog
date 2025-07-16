package com.example.simple_blog.config.security

import com.example.simple_blog.domain.member.Member
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import com.fasterxml.jackson.annotation.JsonProperty

class PrincipalDetails(
    val member: Member
) : UserDetails {

    private val log = KotlinLogging.logger {}

    private var authorityList: MutableList<GrantedAuthority> = mutableListOf(
        SimpleGrantedAuthority("ROLE_${member.role}")
    )

    constructor(
        @JsonProperty("member") member: Member,
        @JsonProperty("authorities") authoritiesJson: List<Map<String, String>>
    ) : this(member) {
        authorityList = authoritiesJson
            .map { SimpleGrantedAuthority(it["authority"]) }
            .toMutableList()
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        log.info("Role 검증")
        return authorityList
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun getUsername(): String {
        return member.email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}