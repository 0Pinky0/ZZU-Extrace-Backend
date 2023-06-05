package com.example.entity.history

import java.time.LocalDateTime

data class PackageHistory(
    val packageId: Int,
    val type: Int,
    val time: LocalDateTime,
    val a: String,
    val b: String,
)