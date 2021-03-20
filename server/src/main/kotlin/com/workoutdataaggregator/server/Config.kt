package com.workoutdataaggregator.server

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.javalin.plugin.json.JavalinJackson

object Config {
    fun init() {
        val objectMapper = initializeObjectMapper()
        JavalinJackson.configure(objectMapper)
    }

    private fun initializeObjectMapper() = ObjectMapper()
        .findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
}