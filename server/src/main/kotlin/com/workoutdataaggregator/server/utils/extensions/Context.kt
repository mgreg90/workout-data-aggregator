package com.workoutdataaggregator.server.utils.extensions

import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
import io.javalin.http.Context
import java.util.*

val UuidRegex = Regex("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")

fun Context.parsePathId(key : String = "id") : Either<Problems.Problem, UUID> {
    val idStr = pathParam(key)
    if (UuidRegex matches idStr) return Either.Value(idStr.toUUIDSafe()!!)

    return Either.Problem(Problems.VALIDATION_ERROR("Failed to parse $key from url"))
}