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

    fun findOne(id: UUID) : Either<Problem, PersonModel> = repository.findById(id)

    fun findAll() = repository.findAll()

    fun create(personDto: PersonCreateDto) : Either<Problem, PersonModel> =
            repository.createOne(PersonModel.Factory().fromCreateDto(personDto))

    fun update(personDto: PersonUpdateDto) : Either<Problem, IModel> =
            repository.updateOne(PersonModel.Factory().fromUpdateDto(personDto))

    fun destroy(id: UUID) : Either<Problem, Boolean> = repository.deleteOne(id)
}