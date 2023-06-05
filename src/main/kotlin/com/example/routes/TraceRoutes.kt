package com.example.routes

import com.example.dao.traceDao
import com.example.entity.Response
import com.example.entity.trace.Trace
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.traceRouting() {
    route("trace") {
        get("get_by_pid/{pid}") {
            val pid = call.parameters.getOrFail<Int>("pid")
            val traces = traceDao.getByPackage(pid)
            call.respond(
                Response(
                    success = true,
                    content = traces,
                )
            )
        }
        get("get_by_eid/{eid}") {
            val eid = call.parameters.getOrFail<Int>("eid")
            val traces = traceDao.getByExpress(eid)
            call.respond(
                Response(
                    success = true,
                    content = traces,
                )
            )
        }
        post("upload") {
            val traceBatch = call.receive<List<Trace>>()
            val result = traceDao.addBatch(traceBatch)
            call.respond(
                Response(
                    success = result,
                    content = result,
                )
            )
        }
        post("upload_single") {
            val trace = call.receive<Trace>()
            val result = traceDao.add(
                trace.packageId,
                trace.lat,
                trace.lng,
                trace.time
            )
            call.respond(
                Response(
                    success = result != null,
                    content = result,
                )
            )
        }
    }
}