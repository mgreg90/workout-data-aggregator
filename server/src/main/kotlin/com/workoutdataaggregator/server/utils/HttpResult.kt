package com.workoutdataaggregator.server.utils

open class HttpResult(val statusCode: Int, val message: String) {
    class NotFound(message: String = NOT_FOUND_MESSAGE) : HttpResult(404, message)
    class ServiceUnavailable(message: String = SERVICE_UNAVAILABLE_MESSAGE) : HttpResult(503, message)


    companion object {
        private const val NOT_FOUND_MESSAGE = "Resource not found"
        private const val SERVICE_UNAVAILABLE_MESSAGE = "Service is temporarily unavailable"
    }
}