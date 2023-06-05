package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object HistoryTable : IntIdTable() {
    val expressId = integer("express_id").references(PackageTable.id).nullable()
    val packageId = integer("package_id").references(PackageTable.id).nullable()
    val type = integer("type")
    val time = datetime("time")
    val a = varchar("a", 64).nullable()
    val b = varchar("b", 64).nullable()
}