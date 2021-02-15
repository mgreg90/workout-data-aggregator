package com.workoutdataaggregator.server

import java.lang.Exception

object Exceptions {
    enum class Types {
        LoginFailure
    }
    class ClientException(override val message : String, val type: Types): Exception(message)

    class MissingEnvVarException(missingEnvVars : List<String>) : Exception(msg(missingEnvVars)) {
        companion object {
            fun msg(missingEnvVars : List<String>) =
                "Missing Environment Variables: ${missingEnvVars.joinToString(", ")}"
        }
    }

    class InvalidEnvVarException(missingEnvVars : List<String>) : Exception(msg(missingEnvVars)) {
        companion object {
            fun msg(invalidEnvVars : List<String>) =
                "Unable to Parse Environment Variables: ${invalidEnvVars.joinToString(", ")}"
        }
    }
}