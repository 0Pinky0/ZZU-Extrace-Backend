package com.example.models

import org.jetbrains.exposed.sql.Table

data class Express(
    val id: Int,
    val content: String,
    val senderId: Int,
    val receiverId: Int,
)

data class ExpressInfo(
    val content: String,
    val senderId: Int,
    val receiverId: Int,
)

object Expresses : Table() {
    // 快件
    val id = integer("id").autoIncrement()
    val content = varchar("content", 128)

    val senderId = integer("sender_id").references(Users.id)
    val receiverId = integer("receiver_id").references(Users.id)

    override val primaryKey = PrimaryKey(id)
}