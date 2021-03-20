package com.workoutdataaggregator.server.rest.controllers

import com.workoutdataaggregator.server.clients.responsedtos.HealthCheckFieldStatus
import com.workoutdataaggregator.server.clients.responsedtos.HealthCheckResponseDto
import com.workoutdataaggregator.server.persistence.MongoDb
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
        val response = HealthCheckResponseDto(
            serverStatus = HealthCheckFieldStatus.Active,
            databaseStatus = databaseStatus()
        )
        ctx.json(response)
    }

    private fun databaseStatus() =
        if (MongoDb.isLive()) HealthCheckFieldStatus.Active else HealthCheckFieldStatus.Inactive
}