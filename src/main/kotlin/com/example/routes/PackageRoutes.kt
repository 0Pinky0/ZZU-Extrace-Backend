package com.example.routes

import com.example.dao.packageDao
import com.example.models.Package
import com.example.models.PackageInfo
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.packageRouting() {
    route("package") {
        getAllPackages()
        addPackage()
        getPackage()
        editPackage()
        deletePackage()
        getContent()
    }
}

private fun Route.getAllPackages() {
    get {
        call.respond(packageDao.getAll())
    }
}

private fun Route.addPackage() {
    post("add") {
        val info = call.receive<PackageInfo>()
        val item = packageDao.add(info)
        call.respond(mapOf("OK" to (item == null)))
    }
}

private fun Route.getPackage() {
    get("get/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        val item = packageDao.get(id)
        call.respond(mapOf("OK" to (item == null)))
    }
}

private fun Route.editPackage() {
    post("edit") {
        val item = call.receive<Package>()
        call.respond(mapOf("OK" to packageDao.edit(item)))
    }
}

private fun Route.deletePackage() {
    post("delete/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        call.respond(mapOf("OK" to packageDao.delete(id)))
    }
}

private fun Route.getContent() {
    get("content/{id}") {
        call.respond(packageDao.getAll())
    }
}