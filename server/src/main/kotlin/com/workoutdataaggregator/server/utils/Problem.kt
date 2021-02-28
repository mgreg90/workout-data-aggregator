package com.workoutdataaggregator.server.utils

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.workoutdataaggregator.server.rest.dtos.ValidationResult
import io.javalin.http.Context

object Problems {
    fun NOT_FOUND(message : String) = Problem(
        type = ProblemType.NOT_FOUND,
        message = message,
        statusCode = 404
    )

    fun NOT_FOUND() = Problem(
        type = ProblemType.NOT_FOUND,
        statusCode = 404
    )

    fun AUTHENTICATION_ERROR() = Problem(
        type = ProblemType.AUTHENTICATION_FAILED,
        statusCode = 401
    )

    fun AUTHENTICATION_ERROR(message : String) = Problem(
        type = ProblemType.AUTHENTICATION_FAILED,
        message = message,
        statusCode = 401
    )

    fun AUTHORIZATION_ERROR() = Problem(
        type = ProblemType.AUTHORIZATION_FAILED,
        statusCode = 403
    )

    fun AUTHORIZATION_ERROR(message : String) = Problem(
        type = ProblemType.AUTHORIZATION_FAILED,
        message = message,
        statusCode = 403
    )

    fun CLIENT_NETWORK_ERROR(message : String) = Problem(
        type = ProblemType.CLIENT_NETWORK_ERROR,
        message = message,
        statusCode = 503
    )

    fun VALIDATION_ERROR(message : String) = Problem(
        type = ProblemType.VALIDATION_ERROR,
        message = message,
        statusCode = 400
    )

    fun VALIDATION_ERROR(results : List<ValidationResult>) = ValidationProblem(
        results = results
    )

    fun INTERNAL_SERVER_ERROR() = Problem(
        type = ProblemType.INTERNAL_SERVER_ERROR,
        statusCode = 500
    )

    fun DATABASE_UNAVAILABLE_ERROR() = Problem(
        type = ProblemType.SERVICE_UNAVAILABLE,
        statusCode = 503
    )


    fun DATABASE_ACTION_FAILED_ERROR(message : String) = Problem(
            type = ProblemType.INTERNAL_SERVER_ERROR,
            message = message,
            statusCode = 500
    )

    open class Problem(
        @JsonIgnore val type : ProblemType,
        val message : String,
        @JsonIgnore val statusCode: Int = 500
    ) {
        constructor(type : ProblemType) : this(type, type.defaultMessage)
        constructor(type : ProblemType, statusCode: Int) : this(type, type.defaultMessage, statusCode)

        @JsonProperty("type") val typeString = type.typeString

        fun renderJson(ctx: Context) {
            ctx.json(this)
            ctx.status(statusCode)
        }
    }

    class ValidationProblem(
        val results : List<ValidationResult>
    ) : Problem(ProblemType.VALIDATION_ERROR, "Validation failed", 400)

    enum class ProblemType(_typeString : String, val defaultMessage : String) {
        NOT_FOUND(
            "not-found",
            "Resource not found"
        ),

        AUTHENTICATION_FAILED(
            "authentication-failed",
            "Authentication Failed"
        ),

        AUTHORIZATION_FAILED(
            "authorization-failed",
            "Authorization Failed"
        ),

        CLIENT_NETWORK_ERROR(
            "client-network-error",
            "Request to 3rd Party Client Failed"
        ),

        VALIDATION_ERROR(
            "validation-error",
            "Validation Failed"
        ),

        INTERNAL_SERVER_ERROR(
            "internal-server-error",
            "Internal server error"
        ),

        SERVICE_UNAVAILABLE(
            "service-unavailable",
            "Service is temporarily unavailable"
        );

        val typeString = "workout-data-aggregator.errors.$_typeString"

        override fun toString() = typeString


    }
}