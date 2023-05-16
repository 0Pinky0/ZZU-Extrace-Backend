package com.example.models

import org.jetbrains.exposed.sql.Table

// 转运节点（网点）
data class TransitNode(
    val id: Int,
    val name: String,
)

object TransitNodeTable : Table() {
    // 转运节点
    val id = integer("id").autoIncrement()
    val name = varchar("name", 64)

    override val primaryKey = PrimaryKey(id)
}