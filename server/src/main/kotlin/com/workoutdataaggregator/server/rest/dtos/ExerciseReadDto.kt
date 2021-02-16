package com.workoutdataaggregator.server.rest.dtos

import io.javalin.http.Context
import org.valiktor.Validator
import org.valiktor.functions.*
import java.time.Instant
import java.util.*

class ExerciseReadDto(val startDate : Instant, val endDate : Instant, val exerciseIds : List<UUID>) : IDto {
    constructor(rawDto : RawExerciseReadDto) : this(rawDto.startDate!!, rawDto.endDate!!, rawDto.exerciseIds)
}

class RawExerciseReadDto(val startDate : Instant?, val endDate : Instant?, val exerciseIds : List<UUID>) : IRawDto {
    // TODO FN-4 Test these validations, they surely need it (esp exercise IDs -> are they UUIDs)
    override fun validations(validator : Validator<IRawDto>) {
        with(validator as Validator<RawExerciseReadDto>) {
            validate(RawExerciseReadDto::startDate).isNotNull().isGreaterThan(Instant.parse("2010-01-01T00:00:00.000Z"))
            validate(RawExerciseReadDto::endDate).isNotNull().isGreaterThan(startDate!!)
            validate(RawExerciseReadDto::exerciseIds).isNotNull().isNotEmpty()
        }
    }
}

class ExerciseReadDtoParser : DtoParserBase() {
    fun parse(ctx : Context) = _parse(ctx, ::ExerciseReadDto)
}