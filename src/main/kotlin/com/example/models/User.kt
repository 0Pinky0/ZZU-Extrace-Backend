package com.example.models

import org.jetbrains.exposed.sql.*

data class User(val uid: Int, val name: String, val password: String)

object Users : Table() {
    val uid = integer("uid").autoIncrement()
    val name = varchar("name", 128)
    val password = varchar("password", 1024)

    override val primaryKey = PrimaryKey(uid)
}