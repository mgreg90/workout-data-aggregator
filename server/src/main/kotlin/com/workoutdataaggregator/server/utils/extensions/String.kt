package com.workoutdataaggregator.server.utils.extensions

import java.util.*

fun String.toUUIDSafe() : UUID? {
    return try {
        UUID.fromString(this)
    } catch(e : Exception) {
        null
    }
}