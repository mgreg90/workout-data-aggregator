package com.workoutdataaggregator.server

import java.lang.Exception

object Exceptions {
    enum class Types {
        LoginFailure
    }
    class ClientException(override val message: String, val type: Types): Exception(message)
}