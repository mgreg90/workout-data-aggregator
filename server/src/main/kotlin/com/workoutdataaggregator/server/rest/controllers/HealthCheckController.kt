package com.workoutdataaggregator.server.rest.controllers

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.http.Context

class HealthCheckController: IController {
    override fun routes() {
        path("/health") {
            get(::read)
        }
    }

    private fun read(ctx: Context) {
        ctx.result("Ok!")
    }
}