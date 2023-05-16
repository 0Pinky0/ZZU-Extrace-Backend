package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.User
import com.example.models.UserBody
import com.example.models.UserTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDao {
    private fun fromResultRow(row: ResultRow) = User(
        id = row[UserTable.id],
        username = row[UserTable.username],
        password = row[UserTable.password],
        firstName = row[UserTable.firstName],
        lastName = row[UserTable.lastName],
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

    suspend fun getByName(username: String): User? = dbQuery {
        UserTable
            .select { UserTable.username eq username }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun login(username: String, password: String): Boolean = dbQuery {
        UserTable
            .select { UserTable.username eq username }
            .map(::fromResultRow)
            .singleOrNull()
            ?.password.equals(password)
    }

    suspend fun add(
        userBody: UserBody
    ): User? = dbQuery {
        try {
            UserTable.insert {
                it[username] = userBody.username
                it[password] = userBody.password
                it[firstName] = userBody.firstName
                it[lastName] = userBody.lastName
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
            UserTable.update({ UserTable.username eq userBody.username }) {
                it[username] = userBody.username
                it[password] = userBody.password
                it[firstName] = userBody.firstName
                it[lastName] = userBody.lastName
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
                    "0Pinky0",
                    "005135",
                    "佳乐",
                    "王",
                    "18853187736"
                )
            )
            add(
                UserBody(
                    "Dovish",
                    "005135",
                    "天浩",
                    "李",
                    "13583167770"
                )
            )
            add(
                UserBody(
                    "Doggy",
                    "123456",
                    "晨",
                    "王",
                    "15513578420"
                )
            )
            add(
                UserBody(
                    "Qtl",
                    "123456",
                    "天乐",
                    "邱",
                    "13753105441"
                )
            )
            add(
                UserBody(
                    "华胥兜率梦曾游",
                    "005136",
                    "欣阳",
                    "孙",
                    "13583167770"
                )
            )
        }
    }
}