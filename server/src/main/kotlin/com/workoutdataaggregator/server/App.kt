package com.workoutdataaggregator.server

import com.workoutdataaggregator.server.clients.Clients
import com.workoutdataaggregator.server.persistence.MongoDb
import com.workoutdataaggregator.server.persistence.repositories.Repositories
import com.workoutdataaggregator.server.rest.Routes
import com.workoutdataaggregator.server.rest.controllers.Controllers
import com.workoutdataaggregator.server.services.Services
import io.javalin.Javalin
import org.slf4j.LoggerFactory

class App {
    private val logger = LoggerFactory.getLogger(javaClass)
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
        logger.debug("Reading Env Vars... ")
        EnvVars.init()
        logger.debug("Reading Env Vars Complete!")
    }

    private fun registerRoutes() {
        logger.debug("Registering Routes... ")
        Routes(controllers).register(app)
        logger.debug("Registering Routes Complete!")
    }

    private fun connectToDb() {
        logger.debug("Creating Database Connection... ")
        MongoDb.init()
        logger.debug("Creating Database Connection Complete!")
    }

    private fun startServer() {
        logger.debug("Starting server on port ${EnvVars.port}... ")
        app.start(EnvVars.port)
        logger.debug("Server is live on port ${EnvVars.port}! 🚀")
    }
}