package com.workoutdataaggregator.server.rest.dtos

import io.javalin.http.Context
import org.valiktor.Validator
import org.valiktor.functions.*
import java.time.Instant
import java.util.*

class ExerciseReadDto(
        val startDate : Instant,
        val endDate : Instant
) : IDto {
    constructor(rawDto : RawExerciseReadDto) : this(rawDto.startDate!!, rawDto.endDate!!)
}

class RawExerciseReadDto(val startDate : Instant?, val endDate : Instant?) : IRawDto {
    override fun validations(validator : Validator<IRawDto>) {
        with(validator as Validator<RawExerciseReadDto>) {
            validate(RawExerciseReadDto::startDate).isNotNull().isGreaterThan(minimumDate)
            validate(RawExerciseReadDto::endDate).isNotNull().isGreaterThan(startDate!!)
        }
    }

    companion object {
        val minimumDate : Instant = Instant.parse("2010-01-01T00:00:00.000Z")
    }
}

class ExerciseReadDtoParser : DtoParserBase() {
    fun parse(ctx : Context) = _parse(ctx, ::ExerciseReadDto)
}