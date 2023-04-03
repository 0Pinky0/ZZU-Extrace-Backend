package com.example.models

import org.jetbrains.exposed.sql.ForeignKeyConstraint
import org.jetbrains.exposed.sql.Table

data class Package(
    val id: Int,
    val name: String,
    val password: String,
    val telephone: String,
)

object Packages : Table() {
    // 用户标识符
    val id = integer("id").autoIncrement()
    val content = varchar("content", 64)
    val senderId = integer("sender_id").references(Users.id)
    val receiverId = integer("receiver_id").references(Users.id)



    val expressId = integer("express_id").references(Expresses.id)

    override val primaryKey = PrimaryKey(id)
}