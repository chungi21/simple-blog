package com.example.simple_blog.config

import com.example.simple_blog.domain.member.Member
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.member.Role
import org.springframework.context.annotation.Configuration
import io.github.serpro69.kfaker.faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.json.JsonWriter
import org.springframework.context.event.EventListener

// Member table의 더미 데이터 넣기 & log Test
@Configuration
class InitData(private val memberRepository: MemberRepository) {

    val faker = faker {}

    private val log = KotlinLogging.logger{}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {

        val member = Member(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        )

        log.info{"inset log-test $member"}

        memberRepository.save(member)

    } // init() end

}