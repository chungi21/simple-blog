package com.example.simple_blog.exception

open class BusinessException(
    val errorCode: ErrorCode,
    message: String? = errorCode.message
) : RuntimeException(message)