package com.example.routes

import com.example.dao.expressDao
import com.example.dao.nodeDao
import com.example.entity.express.Express
import com.example.entity.express.ExpressBody
import com.example.entity.Response
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
        get("search/") {
            val item = expressDao.getAll()
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("search/{key}") {
            val key = "%" + call.parameters.getOrFail<String>("key") + "%"
            val item = expressDao.search(key)
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("get_node/{eid}") {
            val eid = call.parameters.getOrFail<Int>("eid").toInt()
            val item = nodeDao.getExpressNode(eid)
            call.respond(
                Response(
                    success = item != null,
                    content = item
                )
            )
        }
        post("update_node/{eid}/{nid}") {
            val eid = call.parameters.getOrFail<Int>("eid").toInt()
            val nid = call.parameters.getOrFail<Int>("nid").toInt()
            val item = expressDao.updateNode(eid, nid)
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("get_by_src_phone/{phone}") {
            val phone = call.parameters.getOrFail<String>("phone")
            val item = expressDao.getBySrcPhone(phone)
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("get_by_dst_phone/{phone}") {
            val phone = call.parameters.getOrFail<String>("phone")
            val item = expressDao.getByDstPhone(phone)
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        post("state/{id}/{aim}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val aim = call.parameters.getOrFail<Int>("aim").toInt()
            val item = expressDao.stateTransfer(id, aim)
            call.respond(
                Response(
                    success = item,
                    content = item,
                )
            )
        }
        post("state_by_package/{pid}/{aim}") {
            val pid = call.parameters.getOrFail<Int>("pid").toInt()
            val aim = call.parameters.getOrFail<Int>("aim").toInt()
            val item = expressDao.stateTransferByPackage(pid, aim)
            call.respond(
                Response(
                    success = item,
                    content = item,
                )
            )
        }
    }
}

private fun Route.getAllExpresses() {
    get {
        val item = expressDao.getAll()
        call.respond(
            Response(
                success = true,
                content = item
            )
        )
    }
}

private fun Route.addExpress() {
    post("add") {
        val info = call.receive<ExpressBody>()
        val item = expressDao.add(info)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
}

private fun Route.getExpress() {
    get("get/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        val item = expressDao.get(id)
        call.respond(
            Response(
                success = item != null,
                content = item
            )
        )
    }
    get("getp/{id}") {
        val id = call.parameters.getOrFail<Int>("id").toInt()
        val item = expressDao.getByPid(id)
        call.respond(
            Response(
                success = true,
                content = item
            )
        )
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