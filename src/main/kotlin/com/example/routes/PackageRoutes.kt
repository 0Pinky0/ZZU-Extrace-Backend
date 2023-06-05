package com.example.routes

import com.example.dao.expressDao
import com.example.dao.nodeDao
import com.example.dao.packageContentDao
import com.example.dao.packageDao
import com.example.entity.Response
import com.example.entity.node.Node
import com.example.entity.packages.*
import com.example.entity.pkg_ctn.PackageContent
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
        get("search/") {
            val item = packageDao.getAll()
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("search/{key}") {
            val key = "%" + call.parameters.getOrFail<String>("key") + "%"
            val item = packageDao.search(key)
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("state/{id}/{aim}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val aim = call.parameters.getOrFail<Int>("aim").toInt()
            val item = packageDao.stateTransfer(id, aim)
            call.respond(
                Response(
                    success = item,
                    content = item,
                )
            )
        }
        get("get_with_express/{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val packages = packageDao.get(id)
            if (packages == null) {
                call.respond(
                    Response(
                        success = false,
                        content = null,
                    )
                )
            } else {
                val express = expressDao.getByPid(id)
                val content = PackageWithExpress(packages, express)
                call.respond(
                    Response(
                        success = true,
                        content = content,
                    )
                )
            }
        }
        get("get_with_node/{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val packages = packageDao.get(id)
            if (packages == null) {
                call.respond(
                    Response(
                        success = false,
                        content = null,
                    )
                )
            } else {
                val src = nodeDao.get(packages.startId)
                val dst = nodeDao.get(packages.endId)
                if (src == null || dst == null) {
                    call.respond(
                        Response(
                            success = false,
                            content = null,
                        )
                    )
                }
                val content = PackageWithNodes(packages, src!!, dst!!)
                call.respond(
                    Response(
                        success = true,
                        content = content,
                    )
                )
            }
        }
        get("get_with_all/{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val packages = packageDao.get(id)
            if (packages == null) {
                call.respond(
                    Response(
                        success = false,
                        content = null,
                    )
                )
            } else {
                val express = expressDao.getByPid(id)
                val src = nodeDao.get(packages.startId)
                val dst = nodeDao.get(packages.endId)
                if (src == null || dst == null) {
                    call.respond(
                        Response(
                            success = false,
                            content = null,
                        )
                    )
                }
                val content = PackageWithAll(packages, express, src!!, dst!!)
                call.respond(
                    Response(
                        success = true,
                        content = content,
                    )
                )
            }
        }
        post("pkg_ctn") {
            val batch = call.receive<List<PackageContent>>()
            val item = packageContentDao.addBatch(batch)
            call.respond(
                Response(
                    success = item,
                    content = item
                )
            )
        }
    }
}

private fun Route.getAllPackages() {
    get {
        val item = packageDao.getAll()
        call.respond(
            Response(
                success = true,
                content = item
            )
        )
    }
}

private fun Route.addPackage() {
    post("add") {
        val info = call.receive<PackageBody>()
        val item = packageDao.add(info)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
}

private fun Route.getPackage() {
    get("get/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        val item = packageDao.get(id)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
}

private fun Route.getPackageDetailed() {
    get("getinfo/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        val item = packageDao.delete(id)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
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