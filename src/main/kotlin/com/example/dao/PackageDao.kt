package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PackageDao {
    private fun fromResultRow(row: ResultRow) = Package(
        id = row[PackageTable.id],
        content = row[PackageTable.content],
        startId = row[PackageTable.startId],
        endId = row[PackageTable.endId],
    )

    suspend fun getAll(): List<Package> = dbQuery {
        PackageTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Package? = dbQuery {
        PackageTable
            .select { PackageTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        express: PackageBody
    ): Package? = dbQuery {
        // 发送地和目的方不可是同一处
        assert(express.startId != express.endId)
        val insertStatement = PackageTable.insert {
            it[content] = express.content
            it[startId] = express.startId
            it[endId] = express.endId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        express: Package
    ): Boolean = dbQuery {
        PackageTable.update({ PackageTable.id eq express.id }) {
            it[content] = express.content
            it[startId] = express.startId
            it[endId] = express.endId
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        PackageTable.deleteWhere { PackageTable.id eq id } > 0
    }
}

val packageDao: PackageDao = PackageDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(PackageBody("诗社资料1", 1, 3))
            add(PackageBody("诗社资料2", 3, 1))
            add(PackageBody("皮肤", 2, 4))
            add(PackageBody("雨世界", 1, 2))
        }
    }
}