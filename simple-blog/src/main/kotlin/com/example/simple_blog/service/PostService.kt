package com.example.simple_blog.service

import com.example.simple_blog.domain.member.*
import com.example.simple_blog.domain.post.*
import com.example.simple_blog.exception.AccessDeniedCustomException
import com.example.simple_blog.exception.InvalidEmailException
import com.example.simple_blog.exception.InvalidRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository : PostRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findPosts(pageable: Pageable) : Page<PostRes> {
        return postRepository.findPosts(pageable).map { it.toDTO()}
    }

    @Transactional
    fun save(dto: PostSaveReq, member: Member): PostRes {
        // 제목 필수 체크
        if (dto.title.isNullOrBlank()) {
            throw IllegalArgumentException("required input title")
        }

        // 내용 필수 체크
        if (dto.content.isNullOrBlank()) {
            throw IllegalArgumentException("required input content")
        }

        val post = Post(
            title = dto.title,
            content = dto.content,
            member = member
        )
        return postRepository.save(post).toDTO()
    }

    @Transactional
    fun deletePost(postId : Long, member: Member){
        val post = postRepository.findById(postId).orElseThrow {
            throw InvalidRequestException("post does not exist.")
        }

        if (post.member.id != member.id) {
            throw AccessDeniedCustomException("no permission.")
        }

        return postRepository.deleteById(postId)
    }

    @Transactional(readOnly = true)
    fun findPostById(id : Long) : PostRes {
        return postRepository.findById(id).orElseThrow().toDTO()
    }

    @Transactional(readOnly = true)
    fun findPostsByEmail(email: String, pageable: Pageable): Page<PostRes> {
        val member = memberRepository.findMemberByEmailOrNull(email)
            ?: throw InvalidEmailException(email)

        val posts = postRepository.findByMember(member, pageable)

        return posts.map { it.toDTO() }
    }

    @Transactional
    fun updatePost(postId: Long, dto: PostUpdateReq, member: Member): PostRes {
        // 제목 필수 체크
        if (dto.title.isNullOrBlank()) {
            throw IllegalArgumentException("required input title")
        }

        // 내용 필수 체크
        if (dto.content.isNullOrBlank()) {
            throw IllegalArgumentException("required input content")
        }

        val post = postRepository.findById(postId).orElseThrow {
            throw InvalidRequestException("post does not exist.")
        }

        if (post.member.id != member.id) {
            throw AccessDeniedCustomException("no permission.")
        }

        post.update(dto.title, dto.content)

        return post.toDTO()
    }

    @Transactional(readOnly = true)
    fun getPostForEdit(postId: Long, member: Member): PostEditRes {
        val post = postRepository.findById(postId).orElseThrow {
            throw InvalidRequestException("post does not exist.")
        }

        if (post.member.id != member.id) {
            throw AccessDeniedCustomException("no permission.")
        }

        return PostEditRes(
            id = post.id!!,
            title = post.title,
            content = post.content
        )
    }

    @Transactional
    fun deleteByMemberId(memberId: Long) {
        postRepository.deleteByMember_Id(memberId)
    }

}