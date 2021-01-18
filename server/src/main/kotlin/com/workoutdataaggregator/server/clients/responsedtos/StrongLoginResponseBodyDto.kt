package com.workoutdataaggregator.server.clients.responsedtos

data class StrongLoginResponseBodyDto(
    val sessionToken: String = "",
    val objectId: String = ""
)