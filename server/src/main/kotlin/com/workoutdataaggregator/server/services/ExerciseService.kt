package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.persistence.repositories.PersonRepository
import java.time.Instant

class ExerciseService : ServiceBase() {
    fun findWithinDates(startDate : Instant, endDate : Instant) {
        // TODO call strong workout repository to read exercises within dates
    }
//
//    fun findOne(id: UUID) : Either<Problem, IModel> {
//        val person = repository.findById(id)
//        if (person == null) return Either.Problem(Problems.NOT_FOUND())
//        return Either.Value(person)
//    }
//
//    fun findAll() = repository.findAll()
//
//    fun create(personDto: PersonCreateDto) : Either<Problem, IModel> {
//        val createdPerson = repository.createOne(PersonModel.Factory().fromCreateDto(personDto))
//        if (createdPerson != null) return Either.Value(createdPerson)
//
//        return Either.Problem(Problems.DATABASE_ERROR())
//    }
//
//    fun update(personDto: PersonUpdateDto) : Either<Problem, IModel> {
//        val updatedPerson = repository.updateOne(PersonModel.Factory().fromUpdateDto(personDto))
//        if (updatedPerson != null) return Either.Value(updatedPerson)
//
//        return Either.Problem(Problems.DATABASE_ERROR())
//    }
//
//    fun destroy(id: UUID) : Either<Problem, UUID> {
//        val isDeleted = repository.deleteOne(id).deletedCount > 0
//        if (isDeleted) return Either.Value(id)
//
//        return Either.Problem(Problems.DATABASE_ERROR())
//    }
}