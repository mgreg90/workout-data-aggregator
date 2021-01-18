package com.workoutdataaggregator.server.clients

object Clients {
    lateinit var strongClient: StrongClient

    fun init(): Clients {
        strongClient = StrongClient()
        return this
    }
}