package com.example.simple_blog.exception

class InvalidEmailException(email: String) : RuntimeException("$email does not exist")

