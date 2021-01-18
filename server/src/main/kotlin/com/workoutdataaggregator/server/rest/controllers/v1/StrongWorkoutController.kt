package com.workoutdataaggregator.server.rest.controllers.v1

import com.workoutdataaggregator.server.rest.controllers.IController
import com.workoutdataaggregator.server.rest.dtos.StrongWorkoutReadDto
import com.workoutdataaggregator.server.services.StrongService
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.http.Context

class StrongWorkoutController(val strongService: StrongService): IController {
    override fun routes() {
        path("/v1/strong-workout") {
            get(::read)
        }
    }

    private fun read(ctx: Context) {
        val body = ctx.body<StrongWorkoutReadDto>()
        val workouts = strongService.fetchWorkouts(body.startDate)
        ctx.result(workouts)
    }
}