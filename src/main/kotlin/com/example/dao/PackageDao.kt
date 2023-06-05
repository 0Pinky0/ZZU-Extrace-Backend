package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.express.Express
import com.example.entity.packages.Package
import com.example.entity.packages.PackageBody
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PackageDao {
    private fun fromResultRow(row: ResultRow) = Package(
        id = row[PackageTable.id].value,
        state = row[PackageTable.state],
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

    suspend fun search(keyword: String): List<Package> = dbQuery {
        PackageTable
            .join(
                NodeTable,
                JoinType.INNER,
                onColumn = PackageTable.startId,
                otherColumn = NodeTable.id,
            )
            .select {
                concat(
                    PackageTable.id,
                    NodeTable.name,
                    NodeTable.address,
                ) like keyword
            }
            .map(::fromResultRow)
    }

    suspend fun stateTransfer(
        id: Int,
        aimState: Int,
    ): Boolean = dbQuery {
        PackageTable.update({ PackageTable.id eq id }) {
            it[state] = aimState
        } > 0
    }

    suspend fun add(
        express: PackageBody
    ): Package? = dbQuery {
        // 发送地和目的方不可是同一处
        assert(express.startId != express.endId)
        val insertStatement = PackageTable.insert {
            it[state] = express.state
            it[startId] = express.startId
            it[endId] = express.endId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        express: Package
    ): Boolean = dbQuery {
        PackageTable.update({ PackageTable.id eq express.id }) {
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
            add(
                PackageBody(
                    0,
                    1,
                    2,
                )
            )
            add(
                PackageBody(
                    0,
                    2,
                    1,
                )
            )
//            add(PackageBody("诗社资料2", 0, 1))
//            add(PackageBody("皮肤", 0, 4))
//            add(PackageBody("雨世界", 0, 2))
        }
    }
}