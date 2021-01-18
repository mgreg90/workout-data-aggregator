package com.workoutdataaggregator.server.rest

import com.workoutdataaggregator.server.rest.controllers.Controllers
import io.javalin.Javalin

class Routes(private val controllers: Controllers) {
    fun register(app: Javalin) {
        app.routes {
            controllers.all.forEach { it.routes() }
        }
    }
}