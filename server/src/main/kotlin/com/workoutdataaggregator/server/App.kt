package com.workoutdataaggregator.server

import com.workoutdataaggregator.server.clients.Clients
import com.workoutdataaggregator.server.persistence.MongoDb
import com.workoutdataaggregator.server.persistence.repositories.Repositories
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
        configureJavalin()
        startServer()
    }

    private fun readEnvVars() {
        logger.info("Reading Env Vars... ")
        EnvVars.init()
        logger.info("Reading Env Vars Complete!")
    }

    private fun connectToDb() {
        logger.info("Creating Database Connection... ")
        MongoDb.init()
        logger.info("Creating Database Connection Complete!")
    }

    private fun registerRoutes() {
        logger.info("Registering Routes... ")
        controllers.registerRoutes(app)
        logger.info("Registering Routes Complete!")
    }

    private fun configureJavalin() {
        logger.info("Registering Routes... ")
        Config.init()
        logger.info("Registering Routes Complete!")
    }

    private fun startServer() {
        val port = EnvVars.port()

        logger.info("Starting server on port ${port}... ")
        app.start(port)
        logger.info("Server is live on port ${port}! 🚀")
    }
}