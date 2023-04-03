package com.example.models

import org.jetbrains.exposed.sql.Table

data class Express(
    val id: Int,
    val name: String,
    val password: String,
    val telephone: String,
)

object Expresses : Table() {
    // 用户标识符
    val id = integer("id").autoIncrement()
    val senderId = integer("sender_id").references(Users.id)
    val receiverId = integer("receiver_id").references(Users.id)



    override val primaryKey = PrimaryKey(id)
}