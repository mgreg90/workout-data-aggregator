package com.workoutdataaggregator.server.rest.dtos

import io.javalin.http.Context
import org.valiktor.Validator
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull

class PersonCreateDto(val name : String, val age : Int) : IDto {
    constructor(rawDto : RawPersonCreateDto) : this(rawDto.name!!, rawDto.age!!)
}

class RawPersonCreateDto(val name : String?, val age : Int?) : IRawDto {
    override fun validations(validator : Validator<IRawDto>) {
        with(validator as Validator<RawPersonCreateDto>) {
            validate(RawPersonCreateDto::name).isNotNull().isNotBlank()
            validate(RawPersonCreateDto::age).isNotNull().isGreaterThan(0)
        }
    }
}

class PersonCreateDtoParser() : DtoParserBase() {
    fun parse(ctx : Context) = _parse(ctx, ::PersonCreateDto)
}
