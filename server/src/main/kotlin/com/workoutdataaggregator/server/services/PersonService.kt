package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.persistence.models.PersonModel
import com.workoutdataaggregator.server.persistence.repositories.PersonRepository
import com.workoutdataaggregator.server.rest.dtos.PersonCreateDto
import com.workoutdataaggregator.server.rest.dtos.PersonUpdateDto
import java.util.*

class PersonService(private val repository : PersonRepository) {
    fun findOne(id: UUID) = repository.findById(id)
    fun findAll() = repository.findAll()
    fun create(personDto: PersonCreateDto) = repository.createOne(PersonModel.Factory().fromCreateDto(personDto))
    fun update(personDto: PersonUpdateDto) = repository.updateOne(PersonModel.Factory().fromUpdateDto(personDto))
    fun destroy(id: UUID) = repository.deleteOne(id)
}