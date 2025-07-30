package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.comment.CommentRes
import com.example.simple_blog.domain.comment.CommentSaveReq
import com.example.simple_blog.domain.comment.CommentUpdateReq
import com.example.simple_blog.service.CommentService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class CommentController(
	private val commentService: CommentService
) {

	// 댓글 쓰기
	@PostMapping("/comments")
	fun writeComment(
		@RequestBody req: CommentSaveReq,
		@AuthenticationPrincipal user: PrincipalDetails
	): CmResDTO<Any> {
		val result = commentService.write(user.member.id!!, req)
		return CmResDTO(HttpStatus.CREATED, "comment created", result)
	}

	// 댓글 리스트
	@GetMapping("/comments/post/{postId}")
	fun getCommentsByPost(@PathVariable postId: Long): CmResDTO<Any> {
		val comments = commentService.findByPostId(postId)
		return CmResDTO(HttpStatus.OK, "comments fetched", comments)
	}

	// 댓글 수정
	@PutMapping("/comments/{id}")
	fun updateComment(
		@PathVariable id: Long,
		@RequestBody req: CommentUpdateReq,
		@AuthenticationPrincipal user: PrincipalDetails
	): CmResDTO<Any> {
		val updated = commentService.update(user.member.id!!, id, req)
		return CmResDTO(HttpStatus.OK, "comment updated", updated)
	}

	// 댓글 삭제
	@DeleteMapping("/comments/{id}")
	fun deleteComment(
		@PathVariable id: Long,
		@AuthenticationPrincipal user: PrincipalDetails
	): CmResDTO<HttpStatus?> {
		commentService.delete(user.member.id!!, id)
		return CmResDTO(HttpStatus.NO_CONTENT, "comment deleted", null)
	}

}