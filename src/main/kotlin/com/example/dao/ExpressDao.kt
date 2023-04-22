package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ExpressDao {
    private fun fromResultRow(row: ResultRow) = Express(
        id = row[ExpressTable.id],
        content = row[ExpressTable.content],
        senderId = row[ExpressTable.senderId],
        receiverId = row[ExpressTable.receiverId],
    )

    suspend fun getAll(): List<Express> = dbQuery {
        ExpressTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Express? = dbQuery {
        ExpressTable
            .select { ExpressTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        express: ExpressBody
    ): Express? = dbQuery {
        // 发送和接收方不可是同一人
        assert(express.senderId != express.receiverId)
        val insertStatement = ExpressTable.insert {
            it[content] = express.content
            it[senderId] = express.senderId
            it[receiverId] = express.receiverId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        express: Express
    ): Boolean = dbQuery {
        ExpressTable.update({ ExpressTable.id eq express.id }) {
            it[content] = express.content
            it[senderId] = express.senderId
            it[receiverId] = express.receiverId
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        ExpressTable.deleteWhere { ExpressTable.id eq id } > 0
    }

    suspend fun getContent(packageId: Int): List<Express> = dbQuery {
        ((ExpressTable innerJoin PackageContentTable) innerJoin PackageTable)
            .select(PackageTable.id eq packageId)
            .map(::fromResultRow)
    }
}

val expressDao: ExpressDao = ExpressDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(ExpressBody("送天浩", 1, 2))
            add(ExpressBody("济钢诗社", 1, 3))
            add(ExpressBody("马龙论", 3, 1))
            add(ExpressBody("鸽子圆顶笼", 3, 2))
            add(ExpressBody("黑夜使者", 1, 4))
            add(ExpressBody("灵魂莲华", 1, 4))
            add(ExpressBody("雨世界", 1, 2))
            add(ExpressBody("雨世界", 1, 3))
            add(ExpressBody("雨世界", 1, 4))
        }
    }
}