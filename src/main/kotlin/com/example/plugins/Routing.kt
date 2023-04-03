package com.example.plugins

import com.example.routes.expressRouting
import com.example.routes.packageRouting
import com.example.routes.userRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
//        route("misc") {
//            userRouting()
//            expressRouting()
//            packageRouting()
//        }
        userRouting()
        expressRouting()
        packageRouting()

    }
}
