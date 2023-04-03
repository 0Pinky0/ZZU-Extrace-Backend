package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.Location
import com.example.models.Locations
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LocationDao {
    private fun fromResultRow(row: ResultRow) = Location(
        id = row[Locations.id],
        name = row[Locations.name],
    )

    suspend fun getAll(): List<Location> = dbQuery {
        Locations
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Location? = dbQuery {
        Locations
            .select { Locations.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getIdByName(name: String): Int? = dbQuery {
        Locations
            .select { Locations.name eq name }
            .map(::fromResultRow)
            .singleOrNull()
            ?.id
    }

    suspend fun add(
        name: String
    ): Location? = dbQuery {
        val insertStatement = Locations.insert {
            it[Locations.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        node: Location
    ): Boolean = dbQuery {
        Locations.update({ Locations.id eq node.id }) {
            it[name] = node.name
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Locations.deleteWhere { Locations.id eq id } > 0
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