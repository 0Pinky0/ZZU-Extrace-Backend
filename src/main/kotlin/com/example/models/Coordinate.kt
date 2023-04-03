package com.example.models

import org.jetbrains.exposed.sql.Table

data class Coordinate(
    val x: Float,
    val y: Float,
)

object Coordinates : Table() {
    // 用户标识符
    val id = integer("id").autoIncrement()
    val x = float("x")
    val y = float("y")

    override val primaryKey = PrimaryKey(id)
}