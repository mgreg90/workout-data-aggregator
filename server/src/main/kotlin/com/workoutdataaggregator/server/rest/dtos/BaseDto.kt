package com.workoutdataaggregator.server.rest.dtos

import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
import io.javalin.http.Context
import org.slf4j.LoggerFactory
import org.valiktor.ConstraintViolationException
import org.valiktor.Validator
import org.valiktor.i18n.toMessage
import java.lang.Exception

interface IDto

interface IRawDto {
    fun validate() : Either<Problems.Problem, IRawDto> {
        return try {
            val result = org.valiktor.validate(this) {
                validations(this)
            }

            Either.Value(result)
        } catch (e : ConstraintViolationException) {
            val validationResults = e.constraintViolations.map {
                ValidationResult(it.property, it.toMessage().message)
            }

            Either.Problem(Problems.VALIDATION_ERROR(validationResults))
        }
    }

    fun validations(validator : Validator<IRawDto>)
}

interface IDtoParser

abstract class DtoParserBase : IDtoParser {
    val logger = LoggerFactory.getLogger(javaClass)

    inline fun <TDto : IDto, reified TRawDto : IRawDto> _parse(ctx : Context, factory : (TRawDto) -> TDto) : Either<Problems.Problem, TDto> {
        val rawDto : IRawDto?
        try {
            rawDto = ctx.body<TRawDto>()
        } catch (e : Exception) {
            return if (e.message?.startsWith("Couldn't deserialize") == true) {
                val message = "Unable to parse request body"
                logger.error("Failed to parse Request body - ${ctx.body()}")
                Either.Problem(Problems.VALIDATION_ERROR(message))
            } else {
                logger.error("Unhandled Exception! - ${e.javaClass}: ${e.message}")
                Either.Problem(Problems.INTERNAL_SERVER_ERROR())
            }
        }
        val validationResult = rawDto.validate()

        var validatedDto : TRawDto? = null
        when (validationResult) {
            is Either.Problem -> return validationResult
            is Either.Value -> validatedDto = validationResult.value as TRawDto
        }

        val dto = factory(validatedDto)
        return Either.Value(dto)
    }
}

class ValidationResult(val field : String?, val message : String)