package com.example.models

import org.jetbrains.exposed.sql.Table

// 快件
data class Express(
    val id: Int,
    val content: String,
    val senderId: Int,
    val receiverId: Int,
    val srcId: Int,
    val dstId: Int,
)

data class ExpressBody(
    val content: String,
    val senderId: Int,
    val receiverId: Int,
    val srcId: Int,
    val dstId: Int,
)

object ExpressTable : Table() {
    // 快件
    val id = integer("id").autoIncrement()
    val content = varchar("content", 128)

    val senderId = integer("sender_id").references(UserTable.id)
    val receiverId = integer("receiver_id").references(UserTable.id)

    val srcId = integer("src_loc_id").references(LocationTable.id)
    val dstId = integer("dst_loc_id").references(LocationTable.id)

    override val primaryKey = PrimaryKey(id)
}