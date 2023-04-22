package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.Location
import com.example.models.LocationTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LocationDao {
    private fun fromResultRow(row: ResultRow) = Location(
        id = row[LocationTable.id],
        name = row[LocationTable.name],
    )

    suspend fun getAll(): List<Location> = dbQuery {
        LocationTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Location? = dbQuery {
        LocationTable
            .select { LocationTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getIdByName(name: String): Int? = dbQuery {
        LocationTable
            .select { LocationTable.name eq name }
            .map(::fromResultRow)
            .singleOrNull()
            ?.id
    }

    suspend fun add(
        name: String
    ): Location? = dbQuery {
        val insertStatement = LocationTable.insert {
            it[LocationTable.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        node: Location
    ): Boolean = dbQuery {
        LocationTable.update({ LocationTable.id eq node.id }) {
            it[name] = node.name
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        LocationTable.deleteWhere { LocationTable.id eq id } > 0
    }
}

val locationDao: LocationDao = LocationDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add("菊园一号楼")
            add("菊园二号楼")
            add("松园一号楼")
            add("松园二号楼")
            add("柳园一号楼")
            add("柳园二号楼")
            add("荷园一号楼")
            add("荷园二号楼")
        }
    }
}