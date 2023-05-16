package com.example.models

import org.jetbrains.exposed.sql.Table

// 坐标
data class Coordinate(
    val x: Float,
    val y: Float,
)

object CoordinateTable : Table() {
    // 用户标识符
    val id = integer("id").autoIncrement()
    val x = float("x")
    val y = float("y")

    override val primaryKey = PrimaryKey(id)
}