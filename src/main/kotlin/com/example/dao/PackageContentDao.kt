package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.PackageContent
import com.example.models.PackageContents
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PackageContentDao {
    private fun fromResultRow(row: ResultRow) = PackageContent(
        packageId = row[PackageContents.packageId],
        expressId = row[PackageContents.expressId],
    )

    suspend fun getAll(): List<PackageContent> = dbQuery {
        PackageContents
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(
        packageId: Int,
        expressId: Int,
    ): PackageContent? = dbQuery {
        PackageContents
            .select {
                (PackageContents.packageId eq packageId) and
                        (PackageContents.expressId eq expressId)
            }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        packageId: Int,
        expressId: Int,
    ): PackageContent? = dbQuery {
        val insertStatement = PackageContents.insert {
            it[PackageContents.packageId] = packageId
            it[PackageContents.expressId] = expressId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun delete(
        packageId: Int,
        expressId: Int,
    ): Boolean = dbQuery {
        PackageContents.deleteWhere {
            (PackageContents.packageId eq packageId) and
                    (PackageContents.expressId eq expressId)
        } > 0
    }
}

val packageContentDao: PackageContentDao = PackageContentDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(1, 1)
            add(1, 2)
            add(2, 3)
            add(2, 4)
            add(3, 5)
            add(3, 6)
            add(4, 7)
            add(4, 8)
            add(4, 9)
        }
    }
}