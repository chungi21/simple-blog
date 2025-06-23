package com.example.simple_blog.util.value

data class CmResDTO<T> (
    val resultCode : T,
    val resultMsg : String,
    val data : T
)