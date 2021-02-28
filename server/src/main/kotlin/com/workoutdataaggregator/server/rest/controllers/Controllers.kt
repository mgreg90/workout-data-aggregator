package com.workoutdataaggregator.server.rest.controllers

import com.workoutdataaggregator.server.Exceptions
import com.workoutdataaggregator.server.rest.controllers.v1.ExerciseController
import com.workoutdataaggregator.server.rest.controllers.v1.PersonController
import com.workoutdataaggregator.server.rest.controllers.v1.StrongWorkoutController
import com.workoutdataaggregator.server.services.Services
import io.javalin.Javalin

class Controllers(private val services : Services) {
    private var all = listOf<IController>()
    private var isInitialized = false

    fun init() : Controllers {
        val exerciseController = ExerciseController(services.exerciseService)
        val healthCheckController = HealthCheckController()
        val personController = PersonController(services.personService, services.strongService)
        val strongWorkoutController = StrongWorkoutController(services.strongService)

        all = listOf(
            exerciseController,
            healthCheckController,
            personController,
            strongWorkoutController
        )

        isInitialized = true

        return this
    }

    fun registerRoutes(app : Javalin) {
        if (!isInitialized) throw Exceptions.InitializationException(javaClass, "Cannot register routes before initializing controllers")
        app.routes {
            all.forEach { it.routes() }
        }
    }
}