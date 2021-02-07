package com.workoutdataaggregator.server.clients.requestdtos

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.text.SimpleDateFormat
import java.util.*

@JsonSerialize(using = StrongFetchWorkoutsRequestBodyDto.Serializer::class)
data class StrongFetchWorkoutsRequestBodyDto(
    private val userId: String,
    private val startDate: Date,
    private val endDate: Date,
    val include: String = "parseOriginRoutine,parseRoutine,parseSetGroups.parseExercise",
    val limit: String = "1000",
    val method: String = "GET"
) {
    private fun formattedStartDate(): String = formatter.format(startDate)

    private fun formattedEndDate() = formatter.format(endDate)

    fun where() = "{\"user\":{\"__type\":\"Pointer\",\"className\":\"_User\",\"objectId\":\"$userId\"},\"updatedAt\":{\"\$gt\":{\"__type\":\"Date\",\"iso\":\"${formattedStartDate()}\"},\"\$lt\":{\"__type\":\"Date\",\"iso\":\"${formattedEndDate()}\"}}}"

    class Serializer : StdSerializer<StrongFetchWorkoutsRequestBodyDto>(StrongFetchWorkoutsRequestBodyDto::class.java) {

        override fun serialize(
            value: StrongFetchWorkoutsRequestBodyDto?,
            gen: JsonGenerator?,
            provider: SerializerProvider?
        ) {
            gen?.writeStartObject()
            gen?.writeStringField("include", value?.include)
            gen?.writeStringField("limit", value?.limit)
            gen?.writeStringField("where", value?.where())
            gen?.writeStringField("_method", value?.method)
            gen?.writeEndObject()
        }

    }

    companion object {
        val formatter = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'").also { it.timeZone = TimeZone.getTimeZone("UTC") }
    }
}