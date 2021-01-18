package com.workoutdataaggregator.server.clients.responsedtos

import com.workoutdataaggregator.server.EnvVars

data class StrongLoginRequestBodyDto(
    val password: String = EnvVars.strongAppPassword,
    val username: String = EnvVars.strongAppUsername,
    val _method: String = "GET"
)