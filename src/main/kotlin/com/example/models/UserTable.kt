package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable() {
    // 电话号码，登录用
    val telephone = varchar("telephone", 16).uniqueIndex()
    // 用户名
    val username = varchar("username", 32)
    // TODO: 换成哈希后的密码值
    // 登录密码
    val password = varchar("password", 32)
}