package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ExpressDao {
    private fun fromResultRow(row: ResultRow) = Express(
        id = row[Expresses.id],
        content = row[Expresses.content],
        senderId = row[Expresses.senderId],
        receiverId = row[Expresses.receiverId],
    )

    suspend fun getAll(): List<Express> = dbQuery {
        Expresses
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): Express? = dbQuery {
        Expresses
            .select { Expresses.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        express: ExpressInfo
    ): Express? = dbQuery {
        // 发送和接收方不可是同一人
        assert(express.senderId != express.receiverId)
        val insertStatement = Expresses.insert {
            it[content] = express.content
            it[senderId] = express.senderId
            it[receiverId] = express.receiverId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        express: Express
    ): Boolean = dbQuery {
        Expresses.update({ Expresses.id eq express.id }) {
            it[content] = express.content
            it[senderId] = express.senderId
            it[receiverId] = express.receiverId
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Expresses.deleteWhere { Expresses.id eq id } > 0
    }

    suspend fun getContent(packageId: Int): List<Express> = dbQuery {
        ((Expresses innerJoin PackageContents) innerJoin Packages)
            .select(Packages.id eq packageId)
            .map(::fromResultRow)
    }
}

val expressDao: ExpressDao = ExpressDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(ExpressInfo("送天浩", 1, 2))
            add(ExpressInfo("济钢诗社", 1, 3))
            add(ExpressInfo("马龙论", 3, 1))
            add(ExpressInfo("鸽子圆顶笼", 3, 2))
            add(ExpressInfo("黑夜使者", 1, 4))
            add(ExpressInfo("灵魂莲华", 1, 4))
            add(ExpressInfo("雨世界", 1, 2))
            add(ExpressInfo("雨世界", 1, 3))
            add(ExpressInfo("雨世界", 1, 4))
        }
    }
}