package com.example.simple_blog.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.swing.Spring

@RestController
class TestController {

    @GetMapping("/health")
    fun healthTest(): String = "hello kotlin"
}

/*
* dev : aws ec2(프리티어) + s3 + codedeply + github
*
* back : springboot + kotlin + JPA
*
* front : react + typescript + zustand
*
* */