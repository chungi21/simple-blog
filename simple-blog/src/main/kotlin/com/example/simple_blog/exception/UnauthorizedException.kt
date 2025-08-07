package com.example.simple_blog.exception

class UnauthorizedException :
	BusinessException(ErrorCode.UNAUTHORIZED, "Unauthorized access")