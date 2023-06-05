package com.example.entity.packages

// 包裹（运单）
data class Package(
    val id: Int,
    val state: Int,
    val startId: Int,
    val endId: Int,
)