package com.workoutdataaggregator.server

import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory
import java.lang.NumberFormatException

object EnvVars {
    var port : Int = 0
    lateinit var strongAppBaseUrl : String
    lateinit var strongAppUsername : String
    lateinit var strongAppPassword : String
    lateinit var strongAppApplicationId : String

    fun init() {
        val envReader = EnvVarReader()
        port = envReader.readInt("PORT")
        strongAppBaseUrl = envReader.readStr("STRONG_APP_BASE_URL")
        strongAppUsername = envReader.readStr("STRONG_APP_USERNAME")
        strongAppPassword = envReader.readStr("STRONG_APP_PASSWORD")
        strongAppApplicationId = envReader.readStr("STRONG_APP_APPLICATION_ID")

        envReader.validate()
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
        else
            logger.info("Env Vars read successfully!")
    }

    private fun read(varName : String) : String? {
        val value = env.get(varName)
        if (value != null) return value
        missingEnvVars.add(varName)
        return null
    }
}