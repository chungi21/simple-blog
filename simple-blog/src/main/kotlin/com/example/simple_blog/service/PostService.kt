package com.example.simple_blog.service

import com.example.simple_blog.domain.member.*
import com.example.simple_blog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.AccessDeniedException

@Service
class PostService(
    private val postRepository : PostRepository,
    private val memberRepository: MemberRepository
) {

    @Transactional(readOnly = true)
    fun findFposts(pageable: Pageable) : Page<PostRes> {
        return postRepository.findPosts(pageable).map { it.toDTO()}
    }

    @Transactional
    fun save(dto : PostSaveReq): PostRes {
        return postRepository.save(dto.toEntity()).toDTO()
    }

    @Transactional
    fun save(dto: PostSaveReq, member: Member): PostRes {
        val post = Post(
            title = dto.title,
            content = dto.content,
            member = member
        )
        return postRepository.save(post).toDTO()
    }

    @Transactional
    fun deletePost(id : Long){
        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findPostById(id : Long) : PostRes {
        return postRepository.findById(id).orElseThrow().toDTO()
    }

    @Transactional(readOnly = true)
    fun findPostsByEmail(email: String, pageable: Pageable): Page<PostRes> {
        val member = memberRepository.findMemberByEmail(email)
            ?: throw IllegalArgumentException("해당 이메일의 회원을 찾을 수 없습니다: $email")

        val posts = postRepository.findByMember(member, pageable)
        return posts.map { it.toDTO() }
    }


    @Transactional
    fun updatePost(postId: Long, dto: PostUpdateReq, member: Member): PostRes {
        val post = postRepository.findById(postId).orElseThrow {
            throw IllegalArgumentException("게시글이 존재하지 않습니다.")
        }

        if (post.member.id != member.id) {
            throw AccessDeniedException("수정 권한이 없습니다.")
        }

        post.update(dto.title, dto.content)

        return post.toDTO() // 변경감지로 저장됨 (save() 호출 불필요)
    }

    @Transactional(readOnly = true)
    fun getPostForEdit(postId: Long, member: Member): PostEditRes {
        val post = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("해당 게시글이 존재하지 않습니다.") }

        if (post.member.id != member.id) {
            throw AccessDeniedException("수정 권한이 없습니다.")
        }

        return PostEditRes(
            id = post.id!!,
            title = post.title,
            content = post.content
        )
    }

}