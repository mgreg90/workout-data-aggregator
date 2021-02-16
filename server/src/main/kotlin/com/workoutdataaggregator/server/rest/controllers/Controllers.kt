package com.workoutdataaggregator.server.rest.controllers

import com.workoutdataaggregator.server.rest.controllers.v1.ExerciseController
import com.workoutdataaggregator.server.rest.controllers.v1.PersonController
import com.workoutdataaggregator.server.rest.controllers.v1.StrongWorkoutController
import com.workoutdataaggregator.server.services.Services

class Controllers(private val services: Services) {
    var all = listOf<IController>()

    fun init(): Controllers {
        val exerciseController = ExerciseController(services.exerciseService)
        val healthCheckController = HealthCheckController()
        val personController = PersonController(services.personService)
        val strongWorkoutController = StrongWorkoutController(services.strongService)

        all = listOf(
            exerciseController,
            healthCheckController,
            personController,
            strongWorkoutController
        )

        return this
    }
}