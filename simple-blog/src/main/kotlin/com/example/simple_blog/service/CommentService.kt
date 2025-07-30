package com.example.simple_blog.service

import com.example.simple_blog.domain.comment.Comment
import com.example.simple_blog.domain.comment.CommentRepository
import com.example.simple_blog.domain.comment.CommentRes
import com.example.simple_blog.domain.comment.CommentSaveReq
import com.example.simple_blog.domain.member.MemberRepository
import com.example.simple_blog.domain.post.PostRepository
import com.example.simple_blog.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
	private val commentRepository: CommentRepository,
	private val postRepository: PostRepository,
	private val memberRepository: MemberRepository
) {

	@Transactional
	fun write(memberId: Long, req: CommentSaveReq): CommentRes {
		val post = postRepository.findById(req.postId)
			.orElseThrow { EntityNotFoundException("Post not found") }

		val member = memberRepository.findById(memberId)
			.orElseThrow { EntityNotFoundException("Member not found") }

		val comment = Comment(req.content, post, member)
		val saved = commentRepository.save(comment)

		return CommentRes(
			id = saved.id!!,
			content = saved.content,
			writerNickname = saved.member.nickname
		)
	}
}