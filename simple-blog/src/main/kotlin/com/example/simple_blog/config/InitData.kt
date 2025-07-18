package com.example.simple_blog.config

import com.example.simple_blog.domain.member.*
import com.example.simple_blog.domain.post.Post
import com.example.simple_blog.domain.post.PostRepository
import com.example.simple_blog.domain.post.PostSaveReq
import com.example.simple_blog.domain.post.toEntity
import org.springframework.context.annotation.Configuration
import io.github.serpro69.kfaker.faker
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Primary
import org.springframework.context.event.EventListener
import org.springframework.transaction.annotation.Transactional

// Member table의 더미 데이터 넣기 & log Test
@Configuration
class InitData(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository
) {

    val faker = faker {}

    private val log = KotlinLogging.logger{}

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {
        val members = generateMembers(100)
        memberRepository.saveAll(members)

/*        val posts = generatePosts(100)
        postRepository.saveAll(posts)*/


/*
        // 더미 데이터 테스트1 - 더미 데이터 하나 insert
        val member = Member(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        )

        log.info{"inset log-test $member"}

        memberRepository.save(member)
*/
/*
        // 더미 데이터 테스트2 - dto를 entity로 변환해서 넣기 & 더미 데이터 하나 insert
        val member = MemberDTO(
            email = faker.internet.safeEmail(),
            password = "1234",
            role = Role.USER
        )

        log.info{"inset log-test ${member.toEntity()}"}

        memberRepository.save(member.toEntity())
*/
/*

        // 더미 데이터 테스트3 - dto를 entity로 변환해서 넣기 and 더미 데이터 여러 개 insert
        val members = mutableListOf<Member>()

        for(i in 1 .. 100){
            val member = generateMember()
            log.info("insert log-test ${member}")
            members.add(member)
        }

        memberRepository.saveAll(members)

*/
    }

/*
    private fun generateMember() : Member = MemberDTO(
        email = faker.internet.safeEmail(),
        password = "1234",
        role = Role.USER
    ).toEntity()
*/

    private fun generateMembers(cnt:Int) : MutableList<Member> {
        val members = mutableListOf<Member>()

        for(i in 1..cnt){
            val member = generateMember()
            log.info{"insert log-test-member $member"}
            members.add(member)
        }

        return members
    }

    private fun generateMember() : Member = MemberSaveReq(
        email = faker.internet.safeEmail(),
        rawpassword = "1234",
        role = Role.USER
    ).toEntity()

/*    private fun generatePosts(cnt: Int): MutableList<Post> {
        val posts = mutableListOf<Post>()

        for (i in 1..cnt) {
            val post = generatePosts()
            log.info { "insert log-test-post $post" }
            posts.add(post)
        }

        posts.forEach { post ->
            log.info { "post:- ${post.title} , ${post.id} ${post.member} ,${post.content}" }
        }

        return posts
    }*/

/*

    private fun generatePosts(): Post = PostSaveReq(
        title = faker.theExpanse.ships(),
        content = faker.quote.matz(),
        memberId = 1
    ).toEntity()
*/



    
}