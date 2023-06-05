package com.example.models

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object TraceTable : IntIdTable() {
    val packageId = integer("package_id").references(PackageTable.id)

    val lat = double("lat")
    val lng = double("lng")
    val time = datetime("time")
}