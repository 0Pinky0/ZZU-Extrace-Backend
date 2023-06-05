package com.example.entity

data class Response<T> (
    val success: Boolean,
    val content: T,
)