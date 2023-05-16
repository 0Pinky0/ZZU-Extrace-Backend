package com.example.dao

import com.example.models.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
//        val jdbcURL = "jdbc:mysql://110.41.165.169:3306/ExTrace"
        val jdbcURL = "jdbc:mysql://192.168.0.161:3306/ExTrace"
        val user = "root"
        val password = "!23wygcxhwjL"
        val database = Database.connect(jdbcURL, driverClassName, user, password)
        transaction(database) {
            SchemaUtils.create(UserTable)
            userDao
            SchemaUtils.create(LocationTable)
            locationDao
            SchemaUtils.create(TransitNodeTable)
            transitNodeDao
            SchemaUtils.create(ExpressTable)
            expressDao
            SchemaUtils.create(PackageTable)
            packageDao
            SchemaUtils.create(PackageContentTable)
            packageContentDao
            return@transaction
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}