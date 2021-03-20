package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.clients.StrongClient
import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsResponseBodyDto
import com.workoutdataaggregator.server.persistence.models.ExerciseModel
import com.workoutdataaggregator.server.persistence.repositories.ExerciseRepository
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
import java.time.Instant
import java.util.*


class StrongService(
    private val strongClient: StrongClient,
    private val exerciseRepository: ExerciseRepository
) {
    fun fetchWorkouts(startDate: Date, endDate: Date) : Either<Problems.Problem, StrongFetchWorkoutsResponseBodyDto> {
        if (!strongClient.isLoggedIn()) {
            when (val loginResult = strongClient.login()) {
                is Either.Problem -> return loginResult
                is Either.Value -> null
            }
        }

        return strongClient.fetchWorkouts(startDate, endDate)
    }

    fun updateWorkouts() : Either<Problems.Problem, Boolean> {
        val lastUpdated = exerciseRepository.getLastUpdateTime()

        if (lastUpdated > Instant.now().minusSeconds(oneHourInSeconds)) return Either.Value(true)

        val exercises : List<ExerciseModel>
        when (val result = fetchWorkouts(Date.from(lastUpdated), Date.from(Instant.now()))) {
            is Either.Problem -> return result
            is Either.Value -> exercises = result.value.toExerciseModelList()
        }

        exerciseRepository.createOrUpdateMany(exercises)

        return Either.Value(true)
    }

    companion object {
        const val oneHourInSeconds = 3600L
    }
}