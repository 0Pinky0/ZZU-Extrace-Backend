package com.example.routes

import com.example.dao.expressDao
import com.example.dao.nodeDao
import com.example.dao.packageDao
import com.example.dao.traceDao
import com.example.entity.Response
import com.example.entity.trace.Trace
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.nodeRouting() {
    route("node") {
        get {
            val nodes = nodeDao.getAll()
            call.respond(
                Response(
                    success = true,
                    content = nodes,
                )
            )
        }
        get("search/") {
            val item = nodeDao.getAll()
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
        get("search/{key}") {
            val key = "%" + call.parameters.getOrFail<String>("key") + "%"
            val item = nodeDao.search(key)
            call.respond(
                Response(
                    success = true,
                    content = item
                )
            )
        }
    }
}