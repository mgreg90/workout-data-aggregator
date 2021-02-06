package com.workoutdataaggregator.server.rest.controllers.v1

import com.workoutdataaggregator.server.rest.controllers.BaseController
import com.workoutdataaggregator.server.rest.controllers.IController
import com.workoutdataaggregator.server.rest.dtos.*
import com.workoutdataaggregator.server.services.PersonService
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
import com.workoutdataaggregator.server.utils.extensions.*
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import java.util.*

class PersonController(private val personService: PersonService): BaseController(), IController {
    override fun routes() {
        path("/v1/person/:id") {
            get(::read)
            delete(::destroy)
        }
        path("/v1/person") {
            get(::readAll)
            post(::create)
            put(::update)
        }
    }

    private fun read(ctx: Context) {
        controllerAction(ctx) {
            val id = when(val parseResult = ctx.parsePathId()) {
                is Either.Problem -> {
                    parseResult.problem.renderJson(ctx)
                    return@controllerAction
                }
                is Either.Value -> parseResult.value
            }

            when (val result = personService.findOne(id)) {
                is Either.Problem -> ctx.json(result.problem)
                is Either.Value -> ctx.json(result.value)
            }
        }
    }

    private fun readAll(ctx: Context) {
        controllerAction(ctx) {
            val persons = personService.findAll()
            ctx.json(persons)
        }
    }

    private fun create(ctx: Context) {
        controllerAction(ctx) {
            val personCreateDto = when(val validationResult = PersonCreateDtoParser().parse(ctx)) {
                is Either.Problem -> {
                    validationResult.problem.renderJson(ctx)
                    return@controllerAction
                }
                is Either.Value -> validationResult.value as PersonCreateDto
            }

            when (val creationResult = personService.create(personCreateDto)) {
                is Either.Problem -> creationResult.problem.renderJson(ctx)
                is Either.Value -> ctx.json(creationResult.value)
            }
        }
    }

    private fun update(ctx: Context) {
        controllerAction(ctx) {
            val personUpdateDto = when (val validationResult = PersonUpdateDtoParser().parse(ctx)) {
                is Either.Problem -> validationResult.problem.renderJson(ctx).let { return@controllerAction }
                is Either.Value -> validationResult.value
            }

            when (val creationResult = personService.update(personUpdateDto)) {
                is Either.Problem -> creationResult.problem.renderJson(ctx)
                is Either.Value -> ctx.json(creationResult.value)
            }
        }
    }

    private fun destroy(ctx: Context) {
        controllerAction(ctx) {
            val id = when(val parseResult = ctx.parsePathId()) {
                is Either.Problem -> {
                    parseResult.problem.renderJson(ctx)
                    return@controllerAction
                }
                is Either.Value -> parseResult.value
            }

            when(val deleteResult = personService.destroy(id)) {
                is Either.Problem -> deleteResult.problem.renderJson(ctx)
                is Either.Value -> ctx.status(204)
            }
        }
    }
}