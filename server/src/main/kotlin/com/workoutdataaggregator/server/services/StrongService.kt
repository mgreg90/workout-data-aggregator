package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.clients.StrongClient
import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsResponseBodyDto
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.HttpResult
import com.workoutdataaggregator.server.utils.Problems
import java.util.*

class StrongService(val strongClient: StrongClient) {
    fun fetchWorkouts(startDate: Date, endDate: Date): Either<Problems.Problem, StrongFetchWorkoutsResponseBodyDto> {
        if (!strongClient.isLoggedIn()) {
            when (val loginResult = strongClient.login()) {
                is Either.Problem -> return loginResult
                is Either.Value -> null
            }
        }

        return strongClient.fetchWorkouts(startDate, endDate)
    }
}