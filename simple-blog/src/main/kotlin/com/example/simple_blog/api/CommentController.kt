package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.comment.CommentRes
import com.example.simple_blog.domain.comment.CommentSaveReq
import com.example.simple_blog.domain.comment.CommentUpdateReq
import com.example.simple_blog.exception.UnauthorizedException
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
	@PostMapping("/posts/{postId}/comments")
	fun writeComment(
		@RequestBody req: CommentSaveReq,
		@AuthenticationPrincipal principal: PrincipalDetails?
	): CmResDTO<Any> {
		principal ?: throw UnauthorizedException()
		val result = commentService.write(principal.member.id!!, req)
		return CmResDTO(HttpStatus.CREATED, "comment created", result)
	}

	// 댓글 리스트
	@GetMapping("/post/{postId}/comments")
	fun getCommentsByPost(@PathVariable postId: Long): CmResDTO<Any> {
		val comments = commentService.findByPostId(postId)
		return CmResDTO(HttpStatus.OK, "comments fetched", comments)
	}

	// 댓글 수정
	@PutMapping("/comments/{commentId}")
	fun updateComment(
		@PathVariable commentId: Long,
		@RequestBody req: CommentUpdateReq,
		@AuthenticationPrincipal principal: PrincipalDetails?
	): CmResDTO<Any> {
		principal ?: throw UnauthorizedException()
		val updated = commentService.update(principal.member.id!!, commentId, req)
		return CmResDTO(HttpStatus.OK, "comment updated", updated)
	}

	// 댓글 삭제
	@DeleteMapping("/comments/{commentId}")
	fun deleteComment(
		@PathVariable commentId: Long,
		@AuthenticationPrincipal principal: PrincipalDetails?
	): CmResDTO<HttpStatus?> {
		principal ?: throw UnauthorizedException()
		commentService.delete(principal.member.id!!, commentId)
		return CmResDTO(HttpStatus.NO_CONTENT, "comment deleted", null)
	}

}