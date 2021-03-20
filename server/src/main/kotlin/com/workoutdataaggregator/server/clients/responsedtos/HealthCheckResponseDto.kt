package com.workoutdataaggregator.server.clients.responsedtos

data class HealthCheckResponseDto(
    val serverStatus: HealthCheckFieldStatus,
    val databaseStatus: HealthCheckFieldStatus
)

enum class HealthCheckFieldStatus {
    Active, Inactive
}