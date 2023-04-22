package com.example.models

import org.jetbrains.exposed.sql.Table

data class PackageContent(
    val packageId: Int,
    val expressId: Int,
)

object PackageContentTable : Table() {
    val packageId = integer("package_id").references(PackageTable.id)
    val expressId = integer("express_id").references(ExpressTable.id)

    override val primaryKey = PrimaryKey(expressId, packageId)
}