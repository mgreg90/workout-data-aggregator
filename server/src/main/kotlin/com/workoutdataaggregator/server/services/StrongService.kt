package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.Exceptions
import com.workoutdataaggregator.server.clients.StrongClient
import java.util.*

class StrongService(val strongClient: StrongClient) {
    fun fetchWorkouts(startDate: Date): String {
        if (!strongClient.isLoggedIn()) { strongClient.login() }

        try {
            val workouts = strongClient.fetchWorkouts(startDate)
        } catch (ex: Exceptions.ClientException) {

        }

        return "Ok!"
    }
}