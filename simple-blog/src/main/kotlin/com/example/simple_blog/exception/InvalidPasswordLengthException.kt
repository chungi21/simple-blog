package com.example.simple_blog.exception

class InvalidPasswordLengthException(val length: Int)
	: RuntimeException("password short (now password length: $length)")
