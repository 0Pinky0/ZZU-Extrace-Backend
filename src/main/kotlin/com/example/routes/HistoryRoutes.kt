package com.example.routes

import com.example.dao.historyDao
import com.example.entity.Response
import com.example.entity.history.ExpressHistory
import com.example.entity.history.PackageHistory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.historyRouting() {
    route("history") {
        get("get_all") {
            val histories = historyDao.getAll()
            call.respond(
                Response(
                    success = true,
                    content = histories.sortedByDescending { it.time },
                )
            )
        }
        post("add_exp") {
            val hisExp = call.receive<ExpressHistory>()
            val result = historyDao.addExp(hisExp)
            call.respond(
                Response(
                    success = result != null,
                    content = result,
                )
            )
        }
        post("add_pkg") {
            val hisPkg = call.receive<PackageHistory>()
            val result = historyDao.addPkg(hisPkg)
            val result1 = historyDao.addPkgExp(hisPkg)
            call.respond(
                Response(
                    success = result1,
                    content = result1,
                )
            )
        }
        get("get_by_pid/{pid}") {
            val pid = call.parameters.getOrFail<Int>("pid")
            val traces = historyDao.getByPackage(pid)
            call.respond(
                Response(
                    success = true,
                    content = traces.sortedByDescending { it.time },
                )
            )
        }
        get("get_by_eid/{eid}") {
            val eid = call.parameters.getOrFail<Int>("eid")
            val traces1 = historyDao.getByExpress(eid)
            val traces2 = historyDao.getByExpressPackage(eid)
            val traces = traces1 + traces2
            call.respond(
                Response(
                    success = true,
                    content = traces.sortedByDescending { it.time },
                )
            )
        }
    }
}