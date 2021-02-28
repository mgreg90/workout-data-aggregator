package com.workoutdataaggregator.server.rest.controllers.v1

import com.workoutdataaggregator.server.rest.controllers.IController
import com.workoutdataaggregator.server.rest.dtos.StrongWorkoutReadDto
import com.workoutdataaggregator.server.services.StrongService
import com.workoutdataaggregator.server.utils.Either
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context

class StrongWorkoutController(private val strongService: StrongService): IController {
    override fun routes() {
        path("/v1/strong-workout") {
            get(::read)
            put(::update)
        }
    }

    private fun read(ctx: Context) {
        val body = ctx.body<StrongWorkoutReadDto>()

        when (val fetchWorkoutsResult = strongService.fetchWorkouts(body.startDate, body.endDate)) {
            is Either.Problem -> fetchWorkoutsResult.problem.renderJson(ctx)
            is Either.Value -> ctx.json(fetchWorkoutsResult.value)
        }
    }

    private fun update(ctx: Context) {
        when (val updateWorkoutsResult = strongService.updateWorkouts()) {
            is Either.Problem -> updateWorkoutsResult.problem.renderJson(ctx)
            is Either.Value -> ctx.json(updateWorkoutsResult.value)
        }
    }
}