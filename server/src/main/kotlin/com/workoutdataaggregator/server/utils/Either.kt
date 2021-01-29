package com.workoutdataaggregator.server.utils

import com.workoutdataaggregator.server.utils.Problems.Problem as UtilsProblem

sealed class Either<out E : UtilsProblem, out V> {
    data class Problem(val problem: UtilsProblem) : Either<UtilsProblem, Nothing>()
    data class Value<out V>(val value: V) : Either<Nothing, V>()
}