package com.example.entity.express

// 快件
data class Express(
    val id: Int,
    val content: String,
    val state: Int,
    val srcName: String,
    val srcPhone: String,
    val dstName: String,
    val dstPhone: String,
)