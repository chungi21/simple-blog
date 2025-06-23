package com.example.simple_blog.api

import com.example.simple_blog.service.PostService
import com.example.simple_blog.util.value.CmResDTO
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(
    private val postService: PostService
) {

    @GetMapping("/posts")
    fun findPosts(@PageableDefault(size = 10) pageable: Pageable) : CmResDTO<*> {
        return CmResDTO(HttpStatus.OK, "find posts", postService.findFposts(pageable))
    }




}