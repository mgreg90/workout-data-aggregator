package com.workoutdataaggregator.server.clients

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.jackson.objectBody
import com.github.kittinunf.fuel.jackson.responseObject
import com.workoutdataaggregator.server.EnvVars
import com.workoutdataaggregator.server.Exceptions
import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsRequestBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongLoginRequestBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongLoginResponseBodyDto
import java.util.*

class StrongClient: BaseClient() {
    lateinit var userId: String
    lateinit var sessionToken: String

    fun isLoggedIn() = userId != null && sessionToken != null

    fun login() {
        print("Logging into Strong App... ")
        val loginUrl = "${EnvVars.strongAppBaseUrl}/parse/login"

        val request = Fuel.post(loginUrl)
            .objectBody(StrongLoginRequestBodyDto())
            .header("X-Parse-Application-Id" to EnvVars.strongAppApplicationId)
            .responseObject(object: ResponseHandler<StrongLoginResponseBodyDto> {
                override fun success(request: Request, response: Response, value: StrongLoginResponseBodyDto) {
                    userId = value.objectId
                    sessionToken = value.sessionToken
                }

                override fun failure(request: Request, response: Response, error: FuelError) {
                    throw Exceptions.ClientException("Strong Api Login Failed! - ${error.message}", Exceptions.Types.LoginFailure)
                }
            })

        request.join()
        println("Complete!")
    }

    fun fetchWorkouts(startDate: Date) {
        print("Fetching Workouts from Strong App... ")
        val workoutsUrl = "${EnvVars.strongAppBaseUrl}/parse/classes/ParseWorkout"

        val request = Fuel.post(workoutsUrl)
            .objectBody(StrongFetchWorkoutsRequestBodyDto(startDate))
            .header("X-Parse-Session-Token" to sessionToken)
            .header("X-Parse-Application-Id" to EnvVars.strongAppApplicationId)

        println("Complete!")
    }
}