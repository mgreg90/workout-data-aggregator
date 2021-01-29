package com.workoutdataaggregator.server.rest.controllers.v1

import com.workoutdataaggregator.server.rest.controllers.BaseController
import com.workoutdataaggregator.server.rest.controllers.IController
import com.workoutdataaggregator.server.rest.dtos.PersonCreateDto
import com.workoutdataaggregator.server.rest.dtos.PersonCreateDtoValidator
//import com.workoutdataaggregator.server.rest.dtos.PersonCreateDtoValidator
import com.workoutdataaggregator.server.rest.dtos.PersonUpdateDto
import com.workoutdataaggregator.server.rest.dtos.UnvalidatedPersonCreateDto
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
            var result : UnvalidatedPersonCreateDto? = null
            try {
                result = ctx.body<UnvalidatedPersonCreateDto>()
            } catch (e : Exception) {
                var message = if (e.message?.startsWith("Couldn't deserialize") == true)
                    "Unable to parse request body"
                else
                    e.localizedMessage

                Problems.VALIDATION_ERROR(message).renderJson(ctx)
                return@controllerAction
            }

            val validationResult = PersonCreateDtoValidator(result!!).validate<PersonCreateDto>()

            when(validationResult) {
                is Either.Problem -> ctx.json(validationResult.problem)
                is Either.Value -> {
                    val person = personService.create(validationResult.value)
                    ctx.json(person)
                }
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