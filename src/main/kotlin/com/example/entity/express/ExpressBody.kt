package com.example.entity.express

data class ExpressBody(
    val content: String,
    val state: Int,
    val srcName: String,
    val srcPhone: String,
    val dstName: String,
    val dstPhone: String,
)