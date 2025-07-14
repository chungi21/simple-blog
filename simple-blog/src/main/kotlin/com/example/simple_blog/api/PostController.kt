package com.example.simple_blog.api

import com.example.simple_blog.domain.post.PostSaveReq
import com.example.simple_blog.service.PostService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable) : CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find posts", postService.findFposts(pageable))
    }

    @GetMapping("/post/{id}")
    fun findById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "find post by id", postService.findPostById(id))
    }

    @DeleteMapping("/post/{id}")
    fun deleteById(@PathVariable id : Long): CmResDTO<Any> {
        return CmResDTO(HttpStatus.OK, "delete post by id", postService.deletePost(id))
    }

    @PostMapping("/post")
    fun save(@RequestBody dto : PostSaveReq): CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "save post", postService.save(dto))
    }


}