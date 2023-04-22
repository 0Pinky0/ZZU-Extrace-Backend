package com.example.routes

import com.example.dao.expressDao
import com.example.models.Express
import com.example.models.ExpressBody
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.expressRouting() {
    route("express") {
        getAllExpresses()
        addExpress()
        getExpress()
        editExpress()
        deleteExpress()
        getContents()
    }
}

private fun Route.getAllExpresses() {
    get {
        call.respond(expressDao.getAll())
    }
}

private fun Route.addExpress() {
    post("add") {
        val info = call.receive<ExpressBody>()
        val item = expressDao.add(info)
        call.respond(mapOf("OK" to (item == null)))
    }
}

private fun Route.getExpress() {
    get("get/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        val item = expressDao.get(id)
        call.respond(mapOf("OK" to (item == null)))
    }
}

private fun Route.editExpress() {
    post("edit") {
        val item = call.receive<Express>()
        call.respond(mapOf("OK" to expressDao.edit(item)))
    }
}

private fun Route.deleteExpress() {
    post("delete/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respond(mapOf("OK" to expressDao.delete(id)))
    }
}

private fun Route.getContents() {
    get("content/{package_id}") {
        val packageId = call.parameters.getOrFail<Int>("package_id").toInt()
        call.respond(expressDao.getContent(packageId))
    }
}