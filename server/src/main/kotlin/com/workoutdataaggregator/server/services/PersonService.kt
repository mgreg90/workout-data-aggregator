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

class PersonService(private val repository : PersonRepository) : ServiceBase() {

    fun findOne(id: UUID) : Either<Problem, IModel> {
        val person = repository.findById(id)
        if (person == null) return Either.Problem(Problems.NOT_FOUND())
        return Either.Value(person)
    }

    fun findAll() = repository.findAll()

    fun create(personDto: PersonCreateDto) : Either<Problem, IModel> {
        val createdPerson = repository.createOne(PersonModel.Factory().fromCreateDto(personDto))
        if (createdPerson != null) return Either.Value(createdPerson)

        return Either.Problem(Problems.DATABASE_ERROR())
    }

    fun update(personDto: PersonUpdateDto) : Either<Problem, IModel> {
        val updatedPerson = repository.updateOne(PersonModel.Factory().fromUpdateDto(personDto))
        if (updatedPerson != null) return Either.Value(updatedPerson)

        return Either.Problem(Problems.DATABASE_ERROR())
    }

    fun destroy(id: UUID) : Either<Problem, UUID> {
        val isDeleted = repository.deleteOne(id).deletedCount > 0
        if (isDeleted) return Either.Value(id)

        return Either.Problem(Problems.DATABASE_ERROR())
    }
}