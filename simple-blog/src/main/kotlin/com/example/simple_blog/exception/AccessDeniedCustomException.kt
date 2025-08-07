package com.example.simple_blog.exception

class AccessDeniedCustomException(message: String) :
	BusinessException(ErrorCode.HANDLE_ACCESS_DENIED, message)