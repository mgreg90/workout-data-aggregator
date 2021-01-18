package com.workoutdataaggregator.server

import com.workoutdataaggregator.server.clients.Clients
import com.workoutdataaggregator.server.rest.controllers.Controllers
import com.workoutdataaggregator.server.services.Services

fun main() {
    val clients = Clients.init()
    val services = Services(clients).init()
    val controllers = Controllers(services).init()

    val app = App()
    app.start(controllers)
}