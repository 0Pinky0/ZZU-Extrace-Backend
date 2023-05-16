package com.example.models

import org.jetbrains.exposed.sql.Table

// 包裹（运单）
data class Package(
    val id: Int,
    val content: String,
    val startId: Int,
    val endId: Int,
)

data class PackageBody(
    val content: String,
    val startId: Int,
    val endId: Int,
)

object PackageTable : Table() {
    // 包裹
    val id = integer("id").autoIncrement()
    val content = varchar("content", 64)

    val startId = integer("ori_node_id").references(TransitNodeTable.id)
    val endId = integer("dst_node_id").references(TransitNodeTable.id)

    override val primaryKey = PrimaryKey(id)
}