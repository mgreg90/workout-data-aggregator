package com.workoutdataaggregator.server

import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory
import java.lang.NumberFormatException

object EnvVars {
    private var isInitialized = false
    private var port : Int = 0
    private lateinit var strongAppBaseUrl : String
    private lateinit var strongAppUsername : String
    private lateinit var strongAppPassword : String
    private lateinit var strongAppApplicationId : String

    fun init() {
        val envReader = EnvVarReader()
        port = envReader.readInt("PORT")
        strongAppBaseUrl = envReader.readStr("STRONG_APP_BASE_URL")
        strongAppUsername = envReader.readStr("STRONG_APP_USERNAME")
        strongAppPassword = envReader.readStr("STRONG_APP_PASSWORD")
        strongAppApplicationId = envReader.readStr("STRONG_APP_APPLICATION_ID")

        envReader.validate()
        isInitialized = true
    }

    fun port() = get(port)
    fun strongAppBaseUrl() = get(strongAppBaseUrl)
    fun strongAppUsername() = get(strongAppUsername)
    fun strongAppPassword() = get(strongAppPassword)
    fun strongAppApplicationId() = get(strongAppApplicationId)

    private fun <T : Any>get(variable : T) : T {
        if (!isInitialized) throw Exceptions.InitializationException(javaClass, "EnvVars are not yet initialized!")
        return variable
    }
}

class EnvVarReader() {
    private val missingEnvVars = mutableListOf<String>()
    private val invalidEnvVars = mutableListOf<String>()
    private val env = dotenv()
    private val logger = LoggerFactory.getLogger(javaClass)

    fun readInt(varName : String) = try {
        read(varName)?.toInt() ?: 0
    } catch (e : NumberFormatException) {
        invalidEnvVars.add(varName)
        0
    }

    fun readStr(varName : String) = read(varName) ?: ""

    fun validate() {
        if (missingEnvVars.any() || invalidEnvVars.any())
            throw Exceptions.EnvVarException(missingEnvVars = missingEnvVars, invalidEnvVars = invalidEnvVars)
    }

    private fun read(varName : String) : String? {
        val value = env.get(varName)
        if (value != null) return value
        missingEnvVars.add(varName)
        return null
    }
}