package com.example.models

import org.jetbrains.exposed.sql.Table

// 用户
data class User(
    val id: Int,
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val telephone: String,
)

data class UserBody(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val telephone: String,
)

object UserTable : Table() {
    val id = integer("id").autoIncrement()
    // 用户名，登录用
    val username = varchar("username", 32).uniqueIndex()
    // TODO: 换成哈希后的密码值
    // 登录密码
    val password = varchar("password", 32)
    // 用户名
    val firstName = varchar("first_name", 32)
    // 用户姓
    val lastName = varchar("last_name", 32)
    // 电话号码
    val telephone = varchar("telephone", 16)

    override val primaryKey = PrimaryKey(id)
}