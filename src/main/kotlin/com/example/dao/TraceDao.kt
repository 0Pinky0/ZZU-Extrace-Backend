package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.trace.Trace
import com.example.models.ExpressTable
import com.example.models.PackageContentTable
import com.example.models.PackageTable
import com.example.models.TraceTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TraceDao {
    private fun fromResultRow(row: ResultRow) = Trace(
        packageId = row[TraceTable.packageId],
        lat = row[TraceTable.lat],
        lng = row[TraceTable.lng],
        time = row[TraceTable.time],
    )

    suspend fun getAll(): List<Trace> = dbQuery {
        TraceTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun getByPackage(pid: Int): List<Trace> = dbQuery {
        TraceTable
            .select { TraceTable.packageId eq pid }
            .sortedBy { TraceTable.time }
            .map(::fromResultRow)
    }

    suspend fun getByExpress(eid: Int): List<Trace> = dbQuery {
        TraceTable
            .join(PackageContentTable, JoinType.INNER, onColumn = TraceTable.packageId, otherColumn = PackageContentTable.packageId)
            .slice(TraceTable.columns)
            .select { PackageContentTable.expressId eq eid }
            .sortedBy { TraceTable.time }
            .map(::fromResultRow)
    }

    suspend fun add(
        packageId: Int,
        lat: Double,
        lng: Double,
        time: LocalDateTime
    ): Trace? = dbQuery {
        val insertStatement = TraceTable.insert {
            it[TraceTable.packageId] = packageId
            it[TraceTable.lat] = lat
            it[TraceTable.lng] = lng
            it[TraceTable.time] = time
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun addBatch(
        batch: List<Trace>
    ): Boolean = dbQuery {
        val insertStatement = TraceTable.batchInsert(batch) { each ->
            this[TraceTable.packageId] = each.packageId
            this[TraceTable.lat] = each.lat
            this[TraceTable.lng] = each.lng
            this[TraceTable.time] = each.time
        }
        insertStatement.isNotEmpty()
    }
}

val traceDao: TraceDao = TraceDao().apply {
    runBlocking {
        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        if (getAll().isEmpty()) {
            add(1, 113.537759, 34.828629, LocalDateTime.parse("2023-05-10 03:55:00", df))
            add(1, 113.537449, 34.828381, LocalDateTime.parse("2023-05-10 03:55:15", df))
            add(1, 113.537422, 34.827737, LocalDateTime.parse("2023-05-10 03:55:30", df))
            add(1, 113.538244, 34.827477, LocalDateTime.parse("2023-05-10 03:55:45", df))
            add(1, 113.539263, 34.827381, LocalDateTime.parse("2023-05-10 03:56:00", df))
            add(1, 113.539201, 34.826944, LocalDateTime.parse("2023-05-10 03:56:15", df))
            add(1, 113.539569, 34.826592, LocalDateTime.parse("2023-05-10 03:56:30", df))
        }
    }
}