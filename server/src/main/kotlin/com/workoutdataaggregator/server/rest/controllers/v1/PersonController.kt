package com.workoutdataaggregator.server.rest.controllers.v1

import com.workoutdataaggregator.server.rest.controllers.IController
import com.workoutdataaggregator.server.rest.dtos.PersonCreateDto
import com.workoutdataaggregator.server.rest.dtos.PersonUpdateDto
import com.workoutdataaggregator.server.services.PersonService
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.Context
import java.util.*

class PersonController(private val personService: PersonService): IController {
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
        val id = UUID.fromString(ctx.pathParam("id"))

        val person = personService.findOne(id)
        if (person == null)
            ctx.status(404)
        else
            ctx.json(person)
    }

    private fun readAll(ctx: Context) {
        val persons = personService.findAll()
        ctx.json(persons)
    }

    private fun create(ctx: Context) {
        val body = ctx.body<PersonCreateDto>()
        val person = personService.create(body)
        ctx.json(person)
    }

    private fun update(ctx: Context) {
        val body = ctx.body<PersonUpdateDto>()
        val person = personService.update(body)
        ctx.json(person)
    }

    private fun destroy(ctx: Context) {
        val id = UUID.fromString(ctx.pathParam("id"))
        personService.destroy(id)
        ctx.status(204)
    }
}