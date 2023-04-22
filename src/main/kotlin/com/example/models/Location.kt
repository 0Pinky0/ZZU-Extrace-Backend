package com.example.models

import org.jetbrains.exposed.sql.Table

data class Location(
    val id: Int,
    val name: String,
)

object LocationTable : Table() {
    // 快件
    val id = integer("id").autoIncrement()
    val name = varchar("name", 64)

    override val primaryKey = PrimaryKey(id)
}