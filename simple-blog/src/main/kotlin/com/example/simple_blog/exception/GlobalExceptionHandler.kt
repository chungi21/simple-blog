package com.example.simple_blog.exception

import jakarta.persistence.NoResultException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.dao.EmptyResultDataAccessException


@RestControllerAdvice
class GlobalExceptionHandler {

    val log = KotlinLogging.logger { }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e : MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error { "MethodArgumentNotValidException : $e" }
        val of = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity(of, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(e : EntityNotFoundException): ResponseEntity<ErrorResponse> {
        log.error { "handleEntityNotFoundException : $e" }
        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(of, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(NoResultException::class)
    fun handleNoResultException(e : NoResultException): ResponseEntity<ErrorResponse> {
        log.error { "NoResultException : $e" }
        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        log.error { "BusinessException : ${e.message}" }
        val errorResponse = ErrorResponse.of(e.errorCode)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(e: UnauthorizedException): ResponseEntity<ErrorResponse> {
        log.error { "UnauthorizedException : ${e.message}" }
        val errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED)
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessDeniedCustomException::class)
    fun handleAccessDenied(e: AccessDeniedCustomException): ResponseEntity<ErrorResponse> {
        log.error { "AccessDeniedCustomException : ${e.message}" }
        val errorResponse = ErrorResponse.of(e.errorCode)
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(InvalidRequestException::class)
    fun handleInvalidRequestException(e: InvalidRequestException): ResponseEntity<ErrorResponse> {
        log.error { "InvalidRequestException : ${e.message}" }
        val errorResponse = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(InvalidPasswordLengthException::class)
    fun handleInvalidPasswordLengthException(e: InvalidPasswordLengthException): ResponseEntity<ErrorResponse> {
        log.error { "InvalidPasswordLengthException : ${e.message}" }
        val errorResponse = ErrorResponse.of(ErrorCode.PASSWORD_TOO_SHORT)
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

}