package com.example.routes

import com.example.dao.userDao
import com.example.models.UserInfo
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.userRouting() {
    route("user") {
        getAllUsers()
        addUser()
        getUser()
        editUser()
        deleteUser()
        login()
    }
}

private fun Route.getAllUsers() {
    get {
        call.respond(userDao.getAll())
    }
}

private fun Route.addUser() {
    post("add") {
        val user = call.receive<UserInfo>()
        val item = userDao.add(user)
        call.respond(mapOf("OK" to (item == null)))
    }
}

private fun Route.getUser() {
    get("get/{username}") {
        val username = call.parameters.getOrFail<String>("username")
        val item = userDao.get(username)
        call.respond(mapOf("OK" to (item == null)))
    }
}

private fun Route.editUser() {
    post("edit") {
        val user = call.receive<UserInfo>()
        call.respond(mapOf("OK" to userDao.edit(user)))
    }
}

private fun Route.deleteUser() {
    post("delete/{username}") {
        val username = call.parameters.getOrFail<String>("username")
        call.respond(mapOf("OK" to userDao.delete(username)))
    }
}

private fun Route.login() {
    get("login/{username}/{password}") {
        val username = call.parameters.getOrFail<String>("username")
        val password = call.parameters.getOrFail<String>("password")
        call.respond(mapOf("OK" to userDao.login(username, password)))
    }
}