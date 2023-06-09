package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.pkg_ctn.PackageContent
import com.example.entity.trace.Trace
import com.example.models.PackageContentTable
import com.example.models.PackageTable
import com.example.models.TraceTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PackageContentDao {
    private fun fromResultRow(row: ResultRow) = PackageContent(
        packageId = row[PackageContentTable.packageId],
        expressId = row[PackageContentTable.expressId],
    )

    suspend fun getAll(): List<PackageContent> = dbQuery {
        PackageContentTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(
        packageId: Int,
        expressId: Int,
    ): PackageContent? = dbQuery {
        PackageContentTable
            .select {
                (PackageContentTable.packageId eq packageId) and
                        (PackageContentTable.expressId eq expressId)
            }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        packageId: Int,
        expressId: Int,
    ): PackageContent? = dbQuery {
        val insertStatement = PackageContentTable.insert {
            it[PackageContentTable.packageId] = packageId
            it[PackageContentTable.expressId] = expressId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun addBatch(
        batch: List<PackageContent>
    ): Boolean = dbQuery {
        val insertStatement = PackageContentTable.batchInsert(batch) { each ->
            this[PackageContentTable.packageId] = each.packageId
            this[PackageContentTable.expressId] = each.expressId
        }
        insertStatement.isNotEmpty()
    }

    suspend fun delete(
        packageId: Int,
        expressId: Int,
    ): Boolean = dbQuery {
        PackageContentTable.deleteWhere {
            (PackageContentTable.packageId eq packageId) and
                    (PackageContentTable.expressId eq expressId)
        } > 0
    }
}

val packageContentDao: PackageContentDao = PackageContentDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(1, 1)
            add(1, 2)
            add(2, 1)
        }
    }
}