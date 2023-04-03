package com.example.models

import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.Table

data class TransitNode(
    val id: Int,
    val name: String,
    val password: String,
    val telephone: String,
)

object TransitNodes : Table() {
    // 用户标识符
    val id = integer("id").autoIncrement()
    val name = varchar("name", 64)
    val x = float("x")
    val y = float("y")

    override val primaryKey = PrimaryKey(id)
}