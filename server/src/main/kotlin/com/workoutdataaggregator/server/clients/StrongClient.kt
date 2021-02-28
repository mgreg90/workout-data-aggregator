package com.workoutdataaggregator.server.clients

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.jackson.objectBody
import com.github.kittinunf.fuel.jackson.responseObject
import com.workoutdataaggregator.server.EnvVars
import com.workoutdataaggregator.server.clients.requestdtos.StrongFetchWorkoutsRequestBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsResponseBodyDto
import com.workoutdataaggregator.server.clients.requestdtos.StrongLoginRequestBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongLoginResponseBodyDto
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems.Problem
import com.workoutdataaggregator.server.utils.Problems
import java.lang.Exception
import java.util.*

class StrongClient: BaseClient() {
    var userId: String? = null
    var sessionToken: String? = null

    fun isLoggedIn() = userId != null || sessionToken != null

    private fun baseUrl() = EnvVars.strongAppBaseUrl()
    private fun applicationId() = EnvVars.strongAppApplicationId()

    fun login(): Either<Problem, Nothing?> {
        logger.debug("Logging into Strong App... ")
        val loginUrl = "${baseUrl()}/parse/login"
        var result : Either<Problem, Nothing?> = Either.Value(null)

        val request = Fuel.post(loginUrl)
            .objectBody(StrongLoginRequestBodyDto())
            .header(APPLICATION_ID_HEADER to applicationId())
            .responseObject(object: ResponseHandler<StrongLoginResponseBodyDto> {
                override fun success(request: Request, response: Response, value: StrongLoginResponseBodyDto) {
                    userId = value.objectId
                    sessionToken = value.sessionToken
                    logger.debug("Complete!")
                }

                override fun failure(request: Request, response: Response, error: FuelError) {
                    result = Either.Problem(Problems.AUTHORIZATION_ERROR("Login to Strong Api Failed! - ${error.message}"))
                    logger.debug("Failed! - ${error.message}")
                }
            })

        request.join()
        return result
    }

    fun fetchWorkouts(startDate: Date, endDate: Date) : Either<Problem, StrongFetchWorkoutsResponseBodyDto> {
        if (!isLoggedIn()) throw Exception("Cannot fetchWorkouts unless logged in!")
        logger.debug("Fetching Workouts from Strong App... ")

        var result : Either<Problem, StrongFetchWorkoutsResponseBodyDto>? = null

        val workoutsUrl = "${baseUrl()}/parse/classes/ParseWorkout"
        val requestBody = StrongFetchWorkoutsRequestBodyDto(userId!!, startDate, endDate)

        val request = Fuel.post(workoutsUrl)
            .objectBody(requestBody)
            .header(SESSION_TOKEN_HEADER to sessionToken!!)
            .header(APPLICATION_ID_HEADER to applicationId())
            .responseObject(object: ResponseHandler<StrongFetchWorkoutsResponseBodyDto> {
                override fun success(request: Request, response: Response, value: StrongFetchWorkoutsResponseBodyDto) {
                    result = Either.Value(value)
                    logger.debug("Complete!")
                }

                override fun failure(request: Request, response: Response, error: FuelError) {
                    result = Either.Problem(Problems.CLIENT_NETWORK_ERROR("Fetching Workouts from Strong Api Failed! - ${error.message}"))
                    logger.error("Failed! - ${error.message}")
                }
            })

        request.join()
        return result!!
    }

    companion object {
        const val SESSION_TOKEN_HEADER = "X-Parse-Session-Token"
        const val APPLICATION_ID_HEADER = "X-Parse-Application-Id"
    }
}