package com.example.routes

import com.example.dao.expressDao
import com.example.dao.packageDao
import com.example.models.Express
import com.example.models.ExpressInfo
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.serviceRouting() {
    route("service") {
        get("content/{package_id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(mapOf("OK" to packageDao.delete(id)))
        }
    }
}