package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.persistence.repositories.ExerciseRepository
import java.time.Instant

class ExerciseService(private val exerciseRepository: ExerciseRepository) : ServiceBase() {
    fun findWithinDates(startDate : Instant, endDate : Instant) =
        exerciseRepository.findWithinDates(startDate, endDate)
}