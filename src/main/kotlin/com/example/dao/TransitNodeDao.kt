package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.TransitNode
import com.example.models.TransitNodes
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TransitNodeDao {
    private fun fromResultRow(row: ResultRow) = TransitNode(
        id = row[TransitNodes.id],
        name = row[TransitNodes.name],
    )

    suspend fun getAll(): List<TransitNode> = dbQuery {
        TransitNodes
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): TransitNode? = dbQuery {
        TransitNodes
            .select { TransitNodes.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getIdByName(name: String): Int? = dbQuery {
        TransitNodes
            .select { TransitNodes.name eq name }
            .map(::fromResultRow)
            .singleOrNull()
            ?.id
    }

    suspend fun add(
        name: String
    ): TransitNode? = dbQuery {
        val insertStatement = TransitNodes.insert {
            it[TransitNodes.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        node: TransitNode
    ): Boolean = dbQuery {
        TransitNodes.update({ TransitNodes.id eq node.id }) {
            it[name] = node.name
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        TransitNodes.deleteWhere { TransitNodes.id eq id } > 0
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