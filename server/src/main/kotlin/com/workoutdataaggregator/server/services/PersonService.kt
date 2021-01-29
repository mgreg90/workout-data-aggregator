package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.persistence.models.IModel
import com.workoutdataaggregator.server.persistence.models.PersonModel
import com.workoutdataaggregator.server.persistence.repositories.PersonRepository
import com.workoutdataaggregator.server.rest.dtos.PersonCreateDto
import com.workoutdataaggregator.server.rest.dtos.PersonUpdateDto
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems.Problem
import com.workoutdataaggregator.server.utils.Problems
import java.util.*

class PersonService(private val repository : PersonRepository) {

    fun findOne(id: UUID) : Either<Problem, IModel> {
        val person = repository.findById(id)
        if (person == null) return Either.Problem(Problems.NOT_FOUND())
        return Either.Value(person)
    }

    fun findAll() = repository.findAll()
    fun create(personDto: PersonCreateDto) = repository.createOne(PersonModel.Factory().fromCreateDto(personDto))
    fun update(personDto: PersonUpdateDto) = repository.updateOne(PersonModel.Factory().fromUpdateDto(personDto))
    fun destroy(id: UUID) = repository.deleteOne(id)
}