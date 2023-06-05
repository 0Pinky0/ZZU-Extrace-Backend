package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable

object PackageTable : IntIdTable() {
    // 包裹
    val state = integer("state")
    val startId = integer("ori_node_id").references(NodeTable.id)
    val endId = integer("dst_node_id").references(NodeTable.id)
}