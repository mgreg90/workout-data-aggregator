package com.workoutdataaggregator.server

import java.lang.Exception

object Exceptions {
    enum class Types {
        LoginFailure
    }
    class ClientException(override val message : String, val type: Types): Exception(message)

    class EnvVarException(invalidEnvVars : List<String> = listOf(), missingEnvVars: List<String> = listOf()) : Exception(msg(invalidEnvVars, missingEnvVars)) {
        companion object {
            fun msg(invalidEnvVars : List<String>, missingEnvVars : List<String>) : String {
                val missingMsg = "Missing Environment Variables: ${missingEnvVars.joinToString(", ")}"
                val invalidMsg = "Unable to Parse Environment Variables: ${invalidEnvVars.joinToString(", ")}"

                return when {
                    missingEnvVars.any() && invalidEnvVars.any() -> "$missingMsg\n$invalidMsg"
                    missingEnvVars.any() -> missingMsg
                    invalidEnvVars.any() -> invalidMsg
                    else -> "Exception reading environment variables."
                }
            }
        }
    }
}