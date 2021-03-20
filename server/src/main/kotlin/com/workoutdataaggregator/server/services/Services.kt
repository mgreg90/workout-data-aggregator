package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.clients.Clients
import com.workoutdataaggregator.server.persistence.repositories.Repositories

class Services(private val clients: Clients, private val repositories: Repositories) {
    lateinit var strongService : StrongService
    lateinit var personService : PersonService
    lateinit var exerciseService : ExerciseService

    fun init(): Services {
        strongService = StrongService(clients.strongClient, repositories.exerciseRepository)
        personService = PersonService(repositories.personRepository)
        exerciseService = ExerciseService(repositories.exerciseRepository)
        return this
    }
}