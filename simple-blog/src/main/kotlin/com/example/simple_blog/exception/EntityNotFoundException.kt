package com.example.simple_blog.exception

open class EntityNotFoundException(message: String?) : BusinessException(ErrorCode.ENTITY_NOT_FOUND, message)

class MemberNotFoundException(id : String) : EntityNotFoundException("$id not found")