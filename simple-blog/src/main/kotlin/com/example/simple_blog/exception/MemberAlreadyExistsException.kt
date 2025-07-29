package com.example.simple_blog.exception

class MemberAlreadyExistsException(email: String) :
	BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, "$email Email in use")

class NicknameAlreadyExistsException(nickname: String) :
	BusinessException(ErrorCode.NICKNAME_ALREADY_EXISTS, "$nickname Nickname in use")
