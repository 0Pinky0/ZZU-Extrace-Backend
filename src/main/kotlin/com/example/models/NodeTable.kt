package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object NodeTable : IntIdTable() {
    val name = varchar("name", 64)
    val address = varchar("address", 64)

    val lat = double("lat")
    val lng = double("lng")
}

