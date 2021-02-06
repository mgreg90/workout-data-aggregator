package com.workoutdataaggregator.server.rest.dtos

import io.javalin.http.Context
import org.valiktor.Validator
import org.valiktor.functions.isGreaterThan
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import java.util.*

class PersonUpdateDto(val id: UUID, val name: String, val age: Int) : IDto {
    constructor(rawDto : RawPersonUpdateDto) : this(rawDto.id!!, rawDto.name!!, rawDto.age!!)
}

class RawPersonUpdateDto(val name : String?, val age : Int?, val id : UUID?) : IRawDto {
    override fun validations(validator: Validator<IRawDto>) {
        with(validator as Validator<RawPersonUpdateDto>) {
            validate(RawPersonUpdateDto::id).isNotNull()
            validate(RawPersonUpdateDto::name).isNotNull().isNotBlank()
            validate(RawPersonUpdateDto::age).isNotNull().isGreaterThan(0)
        }
    }
}

class PersonUpdateDtoParser : DtoParserBase() {
    fun parse(ctx : Context) = _parse(ctx, ::PersonUpdateDto)
}