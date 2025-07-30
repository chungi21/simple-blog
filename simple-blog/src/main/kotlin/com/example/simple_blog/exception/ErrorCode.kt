package com.example.simple_blog.exception

import faker.com.fasterxml.jackson.annotation.JsonFormat

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode(
    val code: String,
    val message:String
) {
    INVALID_INPUT_VALUE("COO1","invalid input value"),
    ENTITY_NOT_FOUND("COO2","Entity not found"),

    EMAIL_ALREADY_EXISTS("M001", "Email in use"),
    NICKNAME_ALREADY_EXISTS("M002", "Nickname in use"),

    HANDLE_ACCESS_DENIED("A001", "권한이 없습니다.")
}