package com.example.simple_blog.service

import com.example.simple_blog.domain.post.Post
import com.example.simple_blog.domain.post.PostRepository
import com.example.simple_blog.domain.post.PostRes
import com.example.simple_blog.domain.post.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository : PostRepository
) {

    @Transactional(readOnly = true)
    fun findFposts() : List<PostRes> {
        return postRepository.findAll().map { it.toDTO()}
    }
}