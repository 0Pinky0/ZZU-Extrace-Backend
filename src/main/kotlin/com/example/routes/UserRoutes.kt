package com.example.routes

import com.example.dao.packageDao
import com.example.dao.userDao
import com.example.entity.Response
import com.example.entity.user.UserBody
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
        getUserByName()
        editUser()
        deleteUser()
        login()
    }
}

private fun Route.getAllUsers() {
    get {
        val item = userDao.getAll()
        call.respond(
            Response(
                success = true,
                content = item
            )
        )
    }
}

private fun Route.addUser() {
    post("add") {
        val user = call.receive<UserBody>()
        val item = userDao.add(user)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
}

private fun Route.getUser() {
    get("get/{id}") {
        val id = call.parameters.getOrFail<Int>("id")
        val item = userDao.get(id)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
}

private fun Route.getUserByName() {
    get("getbyphone/{telephone}") {
        val telephone = call.parameters.getOrFail<String>("telephone")
        val item = userDao.getByPhone(telephone)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
}

private fun Route.editUser() {
    post("edit") {
        val user = call.receive<UserBody>()
        val item = userDao.edit(user)
        call.respond(
            Response(
                success = item,
                content = item
            )
        )
    }
}

private fun Route.deleteUser() {
    post("delete/{username}") {
        val username = call.parameters.getOrFail<String>("username")
        val item = userDao.delete(username)
        call.respond(
            Response(
                success = item,
                content = item
            )
        )
    }
}

private fun Route.login() {
    post("login/{telephone}/{password}") {
        val telephone = call.parameters.getOrFail<String>("telephone")
        val password = call.parameters.getOrFail<String>("password")
        val item = userDao.getByPhone(telephone)
        call.respond(
            Response(
                success = (item?.password == password),
                content = item
            )
        )
    }
}