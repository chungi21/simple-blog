package com.example.simple_blog.service

import com.example.simple_blog.domain.member.*
import com.example.simple_blog.domain.post.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository : PostRepository
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
    fun deletePost(id : Long){
        return postRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun findPostById(id : Long) : PostRes {
        return postRepository.findById(id).orElseThrow().toDTO()
    }

}