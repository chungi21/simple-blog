package com.example.simple_blog.domain.comment

import com.example.simple_blog.domain.post.Post
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
	fun findAllByPost(post: Post): List<Comment>
}
