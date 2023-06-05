package com.example.models

import com.example.models.PackageTable.references
import org.jetbrains.exposed.dao.id.IntIdTable

object ExpressTable : IntIdTable() {
    // 快件
    val content = varchar("content", 128)
    val state = integer("state")

    val srcName = varchar("src_name", 64)
    val srcPhone = varchar("src_phone", 16)
    val dstName = varchar("dst_id", 64)
    val dstPhone = varchar("dst_phone", 16)
    val nodeId = integer("node_id").references(NodeTable.id).nullable()
}