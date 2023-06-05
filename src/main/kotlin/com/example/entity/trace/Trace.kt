package com.example.entity.trace

import java.time.LocalDateTime

// 地区
data class Trace(
    val packageId: Int,
    val lat: Double,
    val lng: Double,
    val time: LocalDateTime,
)