package com.example.simple_blog.domain.comment

data class CommentSaveReq(
	val postId: Long,
	val content: String
)

data class CommentCreateRes(
	val id: Long,
	val content: String,
	val writerNickname: String
)

data class CommentRes(
	val id: Long,
	val content: String,
	val writerNickname: String,
	val writerId: Long
)

data class CommentUpdateReq(
	val content: String
)