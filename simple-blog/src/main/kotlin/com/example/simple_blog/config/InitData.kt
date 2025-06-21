package com.example.simple_blog.config

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.member.Role
import org.springframework.context.annotation.Configuration
import io.github.serpro69.kfaker.faker
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.json.JsonWriter
import org.springframework.context.event.EventListener


@Configuration
class InitData(private val memberRepository: MemberRepository) {

    val faker = faker {}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val member = Member(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        )

        memberRepository.save(member)

    } // init() end

}