package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.TransitNode
import com.example.models.TransitNodeTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TransitNodeDao {
    private fun fromResultRow(row: ResultRow) = TransitNode(
        id = row[TransitNodeTable.id],
        name = row[TransitNodeTable.name],
    )

    suspend fun getAll(): List<TransitNode> = dbQuery {
        TransitNodeTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): TransitNode? = dbQuery {
        TransitNodeTable
            .select { TransitNodeTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getIdByName(name: String): Int? = dbQuery {
        TransitNodeTable
            .select { TransitNodeTable.name eq name }
            .map(::fromResultRow)
            .singleOrNull()
            ?.id
    }

    suspend fun add(
        name: String
    ): TransitNode? = dbQuery {
        val insertStatement = TransitNodeTable.insert {
            it[TransitNodeTable.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        node: TransitNode
    ): Boolean = dbQuery {
        TransitNodeTable.update({ TransitNodeTable.id eq node.id }) {
            it[name] = node.name
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        TransitNodeTable.deleteWhere { TransitNodeTable.id eq id } > 0
    }
}

val transitNodeDao: TransitNodeDao = TransitNodeDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add("菜鸟驿站-松园")
            add("菜鸟驿站-柳园")
            add("京东网点")
            add("顺丰网点")
        }
    }
}