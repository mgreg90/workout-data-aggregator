package com.workoutdataaggregator.server

import io.github.cdimascio.dotenv.dotenv

object EnvVars {
    var port: Int = 8080
    var strongAppBaseUrl: String = ""
    var strongAppUsername: String = ""
    var strongAppPassword: String = ""
    var strongAppApplicationId: String = ""

    fun init() {
        val dotEnvFile = dotenv()
        port = dotEnvFile["PORT"].toInt()
        strongAppBaseUrl = dotEnvFile["STRONG_APP_BASE_URL"]
        strongAppUsername = dotEnvFile["STRONG_APP_USERNAME"]
        strongAppPassword = dotEnvFile["STRONG_APP_PASSWORD"]
        strongAppApplicationId = dotEnvFile["STRONG_APP_APPLICATION_ID"]
    }
}