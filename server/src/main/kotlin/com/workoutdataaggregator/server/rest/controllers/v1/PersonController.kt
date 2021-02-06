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
import java.lang.Exception
import java.util.UUID.fromString

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
            val rawId = ctx.pathParam("id")
            val id = rawId.toUUIDSafe()

            if (id == null) {
                logger.error("Could not parse id - '$rawId'")
                Problems.VALIDATION_ERROR("Could not parse id").renderJson(ctx)
                return@controllerAction
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
            val validationResult = PersonCreateDtoParser().parse(ctx)

            val personCreateDto : PersonCreateDto
            when(validationResult) {
                is Either.Problem -> {
                    validationResult.problem.renderJson(ctx)
                    return@controllerAction
                }
                is Either.Value -> personCreateDto = validationResult.value as PersonCreateDto
            }

            when (val creationResult = personService.create(personCreateDto)) {
                is Either.Problem -> creationResult.problem.renderJson(ctx)
                is Either.Value -> ctx.json(creationResult.value)
            }
        }
    }

    private fun update(ctx: Context) {
        controllerAction(ctx) {
            val body = ctx.body<PersonUpdateDto>()
            val person = personService.update(body)
            ctx.json(person)
        }
    }

    private fun destroy(ctx: Context) {
        controllerAction(ctx) {
            val id = fromString(ctx.pathParam("id"))
            personService.destroy(id)
            ctx.status(204)
        }
    }
}