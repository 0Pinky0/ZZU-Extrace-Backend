package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.history.History
import com.example.entity.history.ExpressHistory
import com.example.entity.history.PackageHistory
import com.example.models.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryDao {
    private fun fromResultRow(row: ResultRow) = History(
        id = row[HistoryTable.id].value,
        type = row[HistoryTable.type],
        time = row[HistoryTable.time],
        a = row[HistoryTable.a].toString(),
        b = row[HistoryTable.b].toString(),
    )

    suspend fun getAll(): List<History> = dbQuery {
        HistoryTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): History? = dbQuery {
        HistoryTable
            .select { HistoryTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getByPackage(pid: Int): List<History> = dbQuery {
        HistoryTable
            .select { HistoryTable.packageId eq pid }
            .sortedBy { TraceTable.time }
            .map(::fromResultRow)
    }

    suspend fun getByExpressPackage(eid: Int): List<History> = dbQuery {
        HistoryTable
            .join(
                PackageContentTable,
                JoinType.INNER,
                onColumn = HistoryTable.packageId,
                otherColumn = PackageContentTable.packageId
            )
//            .slice(HistoryTable.columns)
            .select { PackageContentTable.expressId eq eid }
            .sortedBy { TraceTable.time }
            .map(::fromResultRow)
    }

    suspend fun getByExpress(eid: Int): List<History> = dbQuery {
        HistoryTable
            .select { HistoryTable.expressId eq eid }
            .sortedBy { TraceTable.time }
            .map(::fromResultRow)
    }

    suspend fun addPkg(
        hisPkg: PackageHistory
    ): History? = dbQuery {
        val insertStatement = HistoryTable.insert {
            it[packageId] = hisPkg.packageId
            it[type] = hisPkg.type
            it[time] = hisPkg.time
            it[a] = hisPkg.a
            it[b] = hisPkg.b
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun addPkgExp(
        hisPkg: PackageHistory
    ): Boolean = dbQuery {
        val exps = PackageTable
            .join(
                PackageContentTable,
                JoinType.INNER,
                onColumn = PackageTable.id,
                otherColumn = PackageContentTable.packageId
            )
            .slice(ExpressTable.id)
            .selectAll()
            .map { it[ExpressTable.id].value }

        val insertStatement = HistoryTable.batchInsert(exps) { exp ->
            this[HistoryTable.expressId] = exp
            this[HistoryTable.type] = hisPkg.type
            this[HistoryTable.time] = hisPkg.time
            this[HistoryTable.a] = hisPkg.a
            this[HistoryTable.b] = hisPkg.b
        }
        insertStatement.isNotEmpty()
    }

    suspend fun addExp(
        hisExp: ExpressHistory
    ): History? = dbQuery {
        val insertStatement = HistoryTable.insert {
            it[expressId] = hisExp.expressId
            it[type] = hisExp.type
            it[time] = hisExp.time
            it[a] = hisExp.a
            it[b] = hisExp.b
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        HistoryTable.deleteWhere { HistoryTable.id eq id } > 0
    }
}

val historyDao: HistoryDao = HistoryDao().apply {
    runBlocking {
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        if (getAll().isEmpty()) {
            addExp(ExpressHistory(1, 1, LocalDateTime.parse("2023-05-10 03:55:13", df), "", ""))
            addPkg(
                PackageHistory(
                    1,
                    2,
                    LocalDateTime.parse("2023-05-10 10:14:13", df),
                    "菜鸟驿站-松园",
                    "菜鸟驿站-柳园"
                )
            )
            addPkg(PackageHistory(1, 3, LocalDateTime.parse("2023-05-10 10:19:13", df), "", ""))
            addPkg(PackageHistory(1, 4, LocalDateTime.parse("2023-05-10 11:24:13", df), "菜鸟驿站-柳园", ""))
            addExp(ExpressHistory(1, 5, LocalDateTime.parse("2023-05-10 14:08:13", df), "蔡承林", "13583167770"))
            addExp(ExpressHistory(1, 6, LocalDateTime.parse("2023-05-10 14:59:13", df), "蔡承林", "13583167770"))
        }
    }
}