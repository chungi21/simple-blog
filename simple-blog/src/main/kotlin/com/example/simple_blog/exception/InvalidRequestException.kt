package com.example.simple_blog.exception

class InvalidRequestException(message: String) :
	BusinessException(ErrorCode.INVALID_INPUT_VALUE, message)