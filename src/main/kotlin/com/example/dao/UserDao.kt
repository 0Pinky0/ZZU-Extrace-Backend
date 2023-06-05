package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.entity.user.User
import com.example.entity.user.UserBody
import com.example.models.UserTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDao {
    private fun fromResultRow(row: ResultRow) = User(
        id = row[UserTable.id].value,
        username = row[UserTable.username],
        password = row[UserTable.password],
        telephone = row[UserTable.telephone],
    )

    suspend fun getAll(): List<User> = dbQuery {
        UserTable
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(id: Int): User? = dbQuery {
        UserTable
            .select { UserTable.id eq id }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getByPhone(telephone: String): User? = dbQuery {
        UserTable
            .select { UserTable.telephone eq telephone }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun add(
        userBody: UserBody
    ): User? = dbQuery {
        try {
            UserTable.insert {
                it[username] = userBody.username
                it[password] = userBody.password
                it[telephone] = userBody.telephone
            }.resultedValues?.singleOrNull()?.let(::fromResultRow)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun edit(
        userBody: UserBody
    ): Boolean = dbQuery {
        try {
            UserTable.update({ UserTable.password eq userBody.password }) {
                it[username] = userBody.username
                it[password] = userBody.password
                it[telephone] = userBody.telephone
            } > 0
        } catch (e: Exception) {
            false
        }
    }

    suspend fun delete(username: String): Boolean = dbQuery {
        try {
            UserTable.deleteWhere { UserTable.username eq username } > 0
        } catch (e: Exception) {
            false
        }
    }
}

val userDao: UserDao = UserDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(
                UserBody(
                    "王佳乐",
                    "005135",
                    "18853187736"
                )
            )
            add(
                UserBody(
                    "李天浩",
                    "005135",
                    "13583167770",
                )
            )
            add(
                UserBody(
                    "王晨",
                    "123456",
                    "15513578420"
                )
            )
            add(
                UserBody(
                    "邱天乐",
                    "123456",
                    "13753105441"
                )
            )
            add(
                UserBody(
                    "孙欣阳",
                    "005136",
                    "13583167770"
                )
            )
        }
    }
}