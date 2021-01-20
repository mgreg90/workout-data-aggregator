package com.workoutdataaggregator.server.utils

sealed class Either<out E : HttpResult, out V> {
    data class Error<out E : HttpResult>(val error: E) : Either<E, Nothing>()
    data class Value<out V>(val value: V) : Either<Nothing, V>()
}