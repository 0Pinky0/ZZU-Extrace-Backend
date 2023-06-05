package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.express.Express
import com.example.entity.express.ExpressBody
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class ExpressDao {
    private fun fromResultRow(row: ResultRow) = Express(
        id = row[ExpressTable.id].value,
        content = row[ExpressTable.content],
        state = row[ExpressTable.state],
        srcName = row[ExpressTable.srcName],
        srcPhone = row[ExpressTable.srcPhone],
        dstName = row[ExpressTable.dstName],
        dstPhone = row[ExpressTable.dstPhone],
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

    suspend fun search(keyword: String): List<Express> = dbQuery {
        ExpressTable
            .select {
                concat(
                    ExpressTable.id,
                    ExpressTable.content,
                    ExpressTable.srcName,
                    ExpressTable.srcPhone,
                    ExpressTable.dstName,
                    ExpressTable.dstPhone,
                ) like keyword
            }
            .map(::fromResultRow)
    }

    suspend fun getBySrcPhone(phone: String): List<Express> = dbQuery {
        ExpressTable
            .select { ExpressTable.srcPhone eq phone }
            .map(::fromResultRow)
    }

    suspend fun getByDstPhone(phone: String): List<Express> = dbQuery {
        ExpressTable
            .select { ExpressTable.dstPhone eq phone }
            .map(::fromResultRow)
    }

    suspend fun getByPid(id: Int): List<Express> = dbQuery {
        ExpressTable
            .join(
                PackageContentTable,
                JoinType.INNER,
                onColumn = ExpressTable.id,
                otherColumn = PackageContentTable.expressId
            )
            .slice(ExpressTable.columns)
            .select { PackageContentTable.packageId eq id }
            .map(::fromResultRow)
    }

    suspend fun add(
        express: ExpressBody
    ): Express? = dbQuery {
        val insertStatement = ExpressTable.insert {
            it[content] = express.content
            it[state] = express.state
            it[srcName] = express.srcName
            it[srcPhone] = express.srcPhone
            it[dstName] = express.dstName
            it[dstPhone] = express.dstPhone
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        express: Express
    ): Boolean = dbQuery {
        ExpressTable.update({ ExpressTable.id eq express.id }) {
            it[content] = express.content
            it[srcName] = express.srcName
            it[srcPhone] = express.srcPhone
            it[dstName] = express.dstName
            it[dstPhone] = express.dstPhone
        } > 0
    }

    suspend fun updateNode(
        eid: Int,
        nid: Int,
    ): Boolean = dbQuery {
        ExpressTable.update({ ExpressTable.id eq eid }) {
            it[nodeId] = nid
        } > 0
    }

    suspend fun stateTransfer(
        id: Int,
        aimState: Int,
    ): Boolean = dbQuery {
        ExpressTable.update({ ExpressTable.id eq id }) {
            it[state] = aimState
        } > 0
    }

    suspend fun stateTransferByPackage(
        pid: Int,
        aimState: Int,
    ): Boolean = dbQuery {
        ExpressTable
            .join(
                PackageContentTable,
                JoinType.INNER,
                onColumn = ExpressTable.id,
                otherColumn = PackageContentTable.expressId
            )
            .update({
                PackageContentTable.packageId eq pid
            }) {
                it[ExpressTable.state] = aimState
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
            add(
                ExpressBody(
                    "送天浩",
                    0,
                    "王佳乐",
                    "18853187736",
                    "李天浩",
                    "13583167770"
                )
            )
            add(
                ExpressBody(
                    "咏浩",
                    0,
                    "王晨",
                    "15615516660",
                    "李天浩",
                    "13583167770"
                )
            )
//            add(ExpressBody("济钢诗社", 1, 3, 1, 6))
//            add(ExpressBody("马龙论", 3, 1, 3, 1))
//            add(ExpressBody("鸽子圆顶笼", 3, 2, 3, 2))
//            add(ExpressBody("黑夜使者", 1, 4, 1, 4))
//            add(ExpressBody("灵魂莲华", 1, 4, 1, 4))
//            add(ExpressBody("雨世界", 1, 2, 1, 3))
//            add(ExpressBody("雨世界", 1, 3, 1, 6))
//            add(ExpressBody("雨世界", 1, 4, 1, 8))
        }
    }
}