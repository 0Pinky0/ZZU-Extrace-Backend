package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.express.Express
import com.example.entity.node.Node
import com.example.entity.packages.Package
import com.example.models.ExpressTable
import com.example.models.NodeTable
import com.example.models.PackageTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NodeDao {
    private fun fromResultRow(row: ResultRow) = Node(
        id = row[NodeTable.id].value,
        name = row[NodeTable.name],
        address = row[NodeTable.address],
        x = row[NodeTable.lat],
        y = row[NodeTable.lng],
    )

    suspend fun getAll(): List<Node> = dbQuery {
        NodeTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Node? = dbQuery {
        NodeTable
            .select { NodeTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun search(keyword: String): List<Node> = dbQuery {
        NodeTable
            .select {
                concat(
                    NodeTable.name,
                    NodeTable.address,
                ) like keyword
            }
            .map(::fromResultRow)
    }

    suspend fun getExpressNode(eid: Int): Node? = dbQuery {
        ExpressTable
            .join(
                NodeTable,
                JoinType.INNER,
                onColumn = ExpressTable.nodeId,
                otherColumn = NodeTable.id
            )
            .slice(NodeTable.columns)
            .select { ExpressTable.id eq eid }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getIdByName(name: String): Int? = dbQuery {
        NodeTable
            .select { NodeTable.name eq name }
            .map(::fromResultRow)
            .singleOrNull()
            ?.id
    }

    suspend fun add(
        name: String,
        address: String,
        lat: Double,
        lng: Double,
    ): Node? = dbQuery {
        val insertStatement = NodeTable.insert {
            it[NodeTable.name] = name
            it[NodeTable.address] = address
            it[NodeTable.lat] = lat
            it[NodeTable.lng] = lng
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        node: Node
    ): Boolean = dbQuery {
        NodeTable.update({ NodeTable.id eq node.id }) {
            it[name] = node.name
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        NodeTable.deleteWhere { NodeTable.id eq id } > 0
    }
}

val nodeDao: NodeDao = NodeDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(
                "郑州大学松园菜鸟驿站",
                "河南省郑州市高新大道100号郑州大学松园",
                34.828628,
                113.537744,
            )
            add(
                "郑州大学柳园菜鸟驿站",
                "河南省郑州市高新大道100号郑州大学柳园",
                34.818342,
                113.536046,
            )
        }
    }
}