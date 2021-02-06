package com.workoutdataaggregator.server

import com.workoutdataaggregator.server.clients.Clients
import com.workoutdataaggregator.server.persistence.MongoDb
import com.workoutdataaggregator.server.persistence.repositories.Repositories
import com.workoutdataaggregator.server.rest.Routes
import com.workoutdataaggregator.server.rest.controllers.Controllers
import com.workoutdataaggregator.server.services.Services
import io.javalin.Javalin

class App {
    lateinit var app: Javalin
    lateinit var controllers: Controllers

    fun start() {
        readEnvVars()
        connectToDb()

        val clients = Clients.init()
        val repositories = Repositories.init().registerIndexes()
        val services = Services(clients, repositories).init()
        controllers = Controllers(services).init()

        app = Javalin.create()

        registerRoutes()
        startServer()
    }

    private fun readEnvVars() {
        print("Reading Env Vars... ")
        EnvVars.init()
        println("Complete!")
    }

    private fun registerRoutes() {
        print("Registering Routes... ")
        Routes(controllers).register(app)
        println("Complete!")
    }

    private fun connectToDb() {
        print("Creating Database Connection... ")
        MongoDb.init()
        println("Complete!")
    }

    private fun startServer() {
        println("Starting server on port ${EnvVars.port}... ")
        app.start(EnvVars.port)
        println("Server is live! 🚀")
    }
}