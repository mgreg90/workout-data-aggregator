package com.workoutdataaggregator.server.rest.controllers

import com.workoutdataaggregator.server.rest.controllers.v1.PersonController
import com.workoutdataaggregator.server.rest.controllers.v1.StrongWorkoutController
import com.workoutdataaggregator.server.services.Services

class Controllers(private val services: Services) {
    var all = listOf<IController>()

    fun init(): Controllers {
        val healthCheckController = HealthCheckController()
        val strongWorkoutController = StrongWorkoutController(services.strongService)
        val personController = PersonController(services.personService)

        all = listOf(
            healthCheckController,
            strongWorkoutController,
            personController
        )

        return this
    }
}