package com.example.models

import org.jetbrains.exposed.sql.Table

data class Package(
    val id: Int,
    val content: String,
    val startId: Int,
    val endId: Int,
)

data class PackageInfo(
    val content: String,
    val startId: Int,
    val endId: Int,
)

object Packages : Table() {
    // 包裹
    val id = integer("id").autoIncrement()
    val content = varchar("content", 64)

    val startId = integer("ori_node_id").references(TransitNodes.id)
    val endId = integer("dst_node_id").references(TransitNodes.id)

    override val primaryKey = PrimaryKey(id)
}