package com.example.simple_blog.api

import com.example.simple_blog.config.security.PrincipalDetails
import com.example.simple_blog.domain.post.PostSaveReq
import com.example.simple_blog.domain.post.PostUpdateReq
import com.example.simple_blog.exception.UnauthorizedException
import com.example.simple_blog.service.PostService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/posts")
@RestController
class PostController(
    private val postService: PostService
) {

    // 게시글 목록 조회(전체)
    @GetMapping("")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable) : CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find posts", postService.findFposts(pageable))
    }

    // 게시글 목록 조회(회원별)
    @GetMapping("/email/{email}")
    fun findPostsByMemberEmail(
        @PathVariable email: String,
        @PageableDefault(size = 10) pageable: Pageable
    ): CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find posts by email", postService.findPostsByEmail(email, pageable))
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    fun findById(@PathVariable postId : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "find post by id", postService.findPostById(postId))
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    fun deleteById(@PathVariable postId : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "delete post by id", postService.deletePost(postId))
    }

    // 게시글 작성
    @PostMapping("")
    fun save(
        @RequestBody dto: PostSaveReq,
        @AuthenticationPrincipal principal: PrincipalDetails
    ): CmResDTO<*> {
        val member = principal.member
        return CmResDTO(HttpStatus.OK, "save post", postService.save(dto, member))
    }

    // 게시글 작성 Form (화면 요청)
    @GetMapping("/form")
    fun writeForm(@AuthenticationPrincipal principal: PrincipalDetails?): CmResDTO<*> {
        principal ?: throw UnauthorizedException()
        val member = principal.member
        return CmResDTO(HttpStatus.OK, "write form", mapOf("writer" to member.toDTO()))
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody dto: PostUpdateReq,
        @AuthenticationPrincipal principal: PrincipalDetails
    ): CmResDTO<*> {
        val member = principal.member
        return CmResDTO(HttpStatus.OK, "update post", postService.updatePost(postId, dto, member))
    }

    // 게시글 수정 Form (화면 요청)
    @GetMapping("/{postId}/edit")
    fun editForm(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: PrincipalDetails
    ): CmResDTO<*> {
        val member = principal.member
        val response = postService.getPostForEdit(postId, member)
        return CmResDTO(HttpStatus.OK, "edit form", response)
    }

}