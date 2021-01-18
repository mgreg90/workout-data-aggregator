package com.workoutdataaggregator.server

import com.workoutdataaggregator.server.rest.Routes
import com.workoutdataaggregator.server.rest.controllers.Controllers
import io.javalin.Javalin

class App {
    lateinit var app: Javalin
    lateinit var controllers: Controllers

    fun start(controllers: Controllers) {
        this.controllers = controllers

        app = Javalin.create()

        readEnvVars()
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

    private fun startServer() {
        println("Starting server on port ${EnvVars.port}... ")
        app.start(EnvVars.port)
        println("Server is live! 🚀")
    }
}