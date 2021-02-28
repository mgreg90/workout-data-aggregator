package com.workoutdataaggregator.server

import java.lang.Exception

object Exceptions {
    enum class Types {
        LoginFailure
    }

    class InitializationException(klass : Class<Any>, override val message : String) : Exception(msg(klass, message)) {
        companion object {
            fun msg(klass : Class<Any>, msg : String) = "InitializationException in class `$klass`: $msg"
        }
    }

    class ClientException(override val message : String, val type: Types): Exception(message)

    class EnvVarException(invalidEnvVars : List<String> = listOf(), missingEnvVars: List<String> = listOf()) : Exception(msg(invalidEnvVars, missingEnvVars)) {
        companion object {
            fun msg(invalidEnvVars : List<String>, missingEnvVars : List<String>) : String {
                val missingMsg by lazy { "Missing Environment Variables: ${missingEnvVars.joinToString(", ")}" }
                val invalidMsg by lazy { "Unable to Parse Environment Variables: ${invalidEnvVars.joinToString(", ")}" }

                return when {
                    missingEnvVars.any() && invalidEnvVars.any() -> "$missingMsg\n$invalidMsg"
                    missingEnvVars.any() -> missingMsg
                    invalidEnvVars.any() -> invalidMsg
                    else -> "Exception reading environment variables"
                }
            }
        }
    }
}