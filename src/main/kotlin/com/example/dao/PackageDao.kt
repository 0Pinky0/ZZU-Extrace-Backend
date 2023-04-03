package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PackageDao {
    private fun fromResultRow(row: ResultRow) = Package(
        id = row[Packages.id],
        content = row[Packages.content],
        startId = row[Packages.startId],
        endId = row[Packages.endId],
    )

    suspend fun getAll(): List<Package> = dbQuery {
        Packages
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Package? = dbQuery {
        Packages
            .select { Packages.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        express: PackageInfo
    ): Package? = dbQuery {
        // 发送地和目的方不可是同一处
        assert(express.startId != express.endId)
        val insertStatement = Packages.insert {
            it[content] = express.content
            it[startId] = express.startId
            it[endId] = express.endId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        express: Package
    ): Boolean = dbQuery {
        Packages.update({ Packages.id eq express.id }) {
            it[content] = express.content
            it[startId] = express.startId
            it[endId] = express.endId
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Packages.deleteWhere { Packages.id eq id } > 0
    }
}

val packageDao: PackageDao = PackageDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(PackageInfo("诗社资料1", 1, 3))
            add(PackageInfo("诗社资料2", 3, 1))
            add(PackageInfo("皮肤", 2, 4))
            add(PackageInfo("雨世界", 1, 2))
        }
    }
}