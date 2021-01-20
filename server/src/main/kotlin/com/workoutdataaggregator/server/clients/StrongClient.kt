package com.workoutdataaggregator.server.clients

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.jackson.objectBody
import com.github.kittinunf.fuel.jackson.responseObject
import com.workoutdataaggregator.server.EnvVars
import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsRequestBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsResponseBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongLoginRequestBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongLoginResponseBodyDto
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.HttpResult
import java.lang.Exception
import java.util.*

class StrongClient: BaseClient() {
    var userId: String? = null
    var sessionToken: String? = null

    fun isLoggedIn() = userId != null || sessionToken != null

    fun login(): Either<HttpResult, Nothing?> {
        print("Logging into Strong App... ")
        val loginUrl = "${EnvVars.strongAppBaseUrl}/parse/login"
        var result : Either<HttpResult, Nothing?> = Either.Value(null)

        val request = Fuel.post(loginUrl)
            .objectBody(StrongLoginRequestBodyDto())
            .header(APPLICATION_ID_HEADER to EnvVars.strongAppApplicationId)
            .responseObject(object: ResponseHandler<StrongLoginResponseBodyDto> {
                override fun success(request: Request, response: Response, value: StrongLoginResponseBodyDto) {
                    userId = value.objectId
                    sessionToken = value.sessionToken
                    println("Complete!")
                }

                override fun failure(request: Request, response: Response, error: FuelError) {
                    val httpResult = HttpResult.ServiceUnavailable("Login to Strong Api Failed! - ${error.message}")
                    result = Either.Error(httpResult)
                    println("Failed! - ${error.message}")
                }
            })

        request.join()
        return result
    }

    fun fetchWorkouts(startDate: Date, endDate: Date) : Either<HttpResult, StrongFetchWorkoutsResponseBodyDto> {
        if (!isLoggedIn()) throw Exception("Cannot fetchWorkouts unless logged in!")
        print("Fetching Workouts from Strong App... ")
        val workoutsUrl = "${EnvVars.strongAppBaseUrl}/parse/classes/ParseWorkout"
        var result : Either<HttpResult, StrongFetchWorkoutsResponseBodyDto>? = null

        val request = Fuel.post(workoutsUrl)
            .objectBody(StrongFetchWorkoutsRequestBodyDto(userId!!, startDate, endDate))
            .header(SESSION_TOKEN_HEADER to sessionToken!!)
            .header(APPLICATION_ID_HEADER to EnvVars.strongAppApplicationId)
            .responseObject(object: ResponseHandler<StrongFetchWorkoutsResponseBodyDto> {
                override fun success(request: Request, response: Response, value: StrongFetchWorkoutsResponseBodyDto) {
                    result = Either.Value(value)
                    println("Complete!")
                }

                override fun failure(request: Request, response: Response, error: FuelError) {
                    result = Either.Error(HttpResult.ServiceUnavailable("Fetching Workouts from Strong Api Failed! - ${error.message}"))
                    println("Failed! - ${error.message}")
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