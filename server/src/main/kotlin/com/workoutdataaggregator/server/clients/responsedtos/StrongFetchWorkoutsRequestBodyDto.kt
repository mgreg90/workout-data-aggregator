package com.workoutdataaggregator.server.clients.responsedtos

import java.text.SimpleDateFormat
import java.util.*

data class StrongFetchWorkoutsRequestBodyDto(
    private val startDate: Date,
    val include: String = "parseOriginRoutine,parseRoutine,parseSetGroups.parseExercise",
    val limit: String = "1000",
    val _method: String = "GET"
) {
    private fun formattedStartDate(): String = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSXXX'Z'").format(startDate)

    fun where() = "{\"user\":{\"__type\":\"Pointer\",\"className\":\"_User\",\"objectId\":\"g8rxmQuBpx\"},\"updatedAt\":{\"\$gt\":{\"__type\":\"Date\",\"iso\":\"${formattedStartDate()}\"}}}"
}