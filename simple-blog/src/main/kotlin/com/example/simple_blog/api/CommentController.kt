package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.comment.CommentRes
import com.example.simple_blog.domain.comment.CommentSaveReq
import com.example.simple_blog.service.CommentService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class CommentController(
	private val commentService: CommentService
) {

	@PostMapping("/comments")
	fun writeComment(
		@RequestBody req: CommentSaveReq,
		@AuthenticationPrincipal user: PrincipalDetails
	): CmResDTO<Any> {
		val result = commentService.write(user.member.id!!, req)
		return CmResDTO(HttpStatus.CREATED, "comment created", result)
	}
}