package com.example.models

import org.jetbrains.exposed.sql.Table

data class PackageContent(
    val packageId: Int,
    val expressId: Int,
)

object PackageContents : Table() {
    val packageId = integer("package_id").references(Packages.id)
    val expressId = integer("express_id").references(Expresses.id)

    override val primaryKey = PrimaryKey(expressId, packageId)
}