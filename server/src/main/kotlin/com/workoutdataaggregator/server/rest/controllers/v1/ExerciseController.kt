package com.workoutdataaggregator.server.rest.controllers.v1

import com.workoutdataaggregator.server.rest.controllers.BaseController
import com.workoutdataaggregator.server.rest.controllers.IController
import com.workoutdataaggregator.server.rest.dtos.*
import com.workoutdataaggregator.server.services.ExerciseService
import com.workoutdataaggregator.server.utils.Either
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.http.Context

class ExerciseController(private val exerciseService : ExerciseService) : BaseController(), IController {
    override fun routes() {
        path("/v1/exercises") {
            get(::read)
        }
    }

    private fun read(ctx: Context) {
        controllerAction(ctx) {
            val exerciseReadDto = when(val validationResult = ExerciseReadDtoParser().parse(ctx)) {
                is Either.Problem -> {
                    validationResult.problem.renderJson(ctx)
                    return@controllerAction
                }
                is Either.Value -> validationResult.value
            }

            when (val result = exerciseService.findWithinDates(exerciseReadDto.startDate, exerciseReadDto.endDate)) {
                is Either.Problem -> result.problem.renderJson(ctx)
                is Either.Value -> ctx.json(result.value)
            }
        }
    }
}