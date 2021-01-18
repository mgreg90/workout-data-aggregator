package com.workoutdataaggregator.server.services

import com.workoutdataaggregator.server.clients.Clients

class Services(private val clients: Clients) {
    lateinit var strongService: StrongService

    fun init(): Services {
        strongService = StrongService(clients.strongClient)
        return this
    }
}