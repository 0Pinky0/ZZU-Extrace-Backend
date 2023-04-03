package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.User
import com.example.models.UserNoId
import com.example.models.Users
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDao {
    private fun fromResultRow(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        telephone = row[Users.telephone],
    )

    suspend fun getAll(): List<User> = dbQuery {
        Users
            .selectAll()
            .map(::fromResultRow)
    }

    suspend fun get(username: String): User? = dbQuery {
        Users
            .select { Users.username eq username }
            .map(::fromResultRow)
            .singleOrNull()
    }

    suspend fun getId(username: String): Int? = dbQuery {
        Users
            .select { Users.username eq username }
            .map(::fromResultRow)
            .singleOrNull()
            ?.id
    }

    suspend fun login(username: String, password: String): Boolean = dbQuery {
        Users
            .select { Users.username eq username }
            .map(::fromResultRow)
            .singleOrNull()
            ?.password.equals(password)
    }

    suspend fun add(
        userNoId: UserNoId
    ): User? = dbQuery {
        val insertStatement = Users.insert {
            it[username] = userNoId.username
            it[password] = userNoId.password
            it[firstName] = userNoId.firstName
            it[lastName] = userNoId.lastName
            it[telephone] = userNoId.telephone
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::fromResultRow)
    }

    suspend fun edit(
        userNoId: UserNoId
    ): Boolean = dbQuery {
        Users.update({ Users.username eq userNoId.username }) {
            it[username] = userNoId.username
            it[password] = userNoId.password
            it[firstName] = userNoId.firstName
            it[lastName] = userNoId.lastName
            it[telephone] = userNoId.telephone
        } > 0
    }

    suspend fun delete(username: String): Boolean = dbQuery {
        Users.deleteWhere { Users.username eq username } > 0
    }
}

val userDao: UserDao = UserDao().apply {
    runBlocking {
        if (getAll().isEmpty()) {
            add(
                UserNoId(
                    "0Pinky0",
                    "005135",
                    "佳乐",
                    "王",
                    "18853187736"
                )
            )
        }
    }
}