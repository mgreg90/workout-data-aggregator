package com.workoutdataaggregator.server.rest.dtos

import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems

class ValidationResults(val results : List<ValidationResult>)

class ValidationResult(val field : String?, val message : String)

class Rule<T>(val logic : (T) -> Boolean, val field : String?, val message : String) {
    fun evaluate(input: T) : ValidationResult? {
        if (!logic(input)) return ValidationResult(field, message)
        return null
    }
}

abstract class DtoValidatorBase(val unvalidatedDtoBase : IUnvalidatedDtoBase<IDtoBase>) : IDtoValidatorBase {
    override fun <T : IDtoBase> validate(): Either<Problems.Problem, T> {
        val results : MutableList<ValidationResult> = mutableListOf()

        rules.forEach { rule ->
            val result = rule.evaluate(unvalidatedDtoBase)
            if (result != null) results.add(result)
        }

        return if (results.any())
            Either.Problem(Problems.VALIDATION_ERROR(results))
        else
            Either.Value(unvalidatedDtoBase.toDto() as T)
    }
}

abstract class UnvalidatedDtoBase() : IUnvalidatedDtoBase<IDtoBase>

abstract class DtoBase : IDtoBase

interface IDtoValidatorBase {
    val rules : List<Rule<IUnvalidatedDtoBase<IDtoBase>>>
    fun <T : IDtoBase> validate() : Either<Problems.Problem, IDtoBase>
}

interface IUnvalidatedDtoBase<T : IDtoBase> {
    fun toDto() : T
}

interface IDtoBase


//import com.workoutdataaggregator.server.utils.Either
//import com.workoutdataaggregator.server.utils.Problems
//import io.javalin.http.Context
//import java.lang.Exception
//import kotlin.reflect.KClass
//
//interface IDto {
//}
//
//interface IDtoValidator {
//    fun validations() : List<Validation<out IUnvalidatedDto>>
//}
//
//interface IUnvalidatedDto {
//    fun toDto() : IDto
//}
//
//abstract class BaseDtoValidator : IDtoValidator {
//    inline fun <reified T : IUnvalidatedDto>validateAndGetDto(ctx : Context) : Either<Problems.Problem, IDto> {
//        return try {
//            val bodyValidator = ctx.bodyValidator<T>()
//            validations().forEach { bodyValidator.check(it.test, it.failMessage) }
//            val x = bodyValidator.get().toDto()
//            Either.Value(x)
//        } catch (e : Exception) {
//            var message = if (e.message?.startsWith("Couldn't deserialize") == true)
//                "Unable to parse request body"
//            else
//                e.localizedMessage.split(" - ").drop(1).joinToString()
//
//            Either.Problem(Problems.VALIDATION_ERROR(message))
//        }
//    }
//}
//
////abstract class BaseDto {
////    companion object {
////        protected val validator : BaseDtoValidator?
////        fun fromContext(ctx : Context) : Either<Problems.Problem, BaseDto> = try {
////            val
////        }
////
////        fun <T : BaseDtoValidator>validateAndGet(ctx: Context) : BaseDtoValidator {
////            val bodyValidator = ctx.bodyValidator<BaseDtoValidator>()
////            val validations = T::class.companionObject.
////            _validator.validations().forEach { _validator.check(it.test, it.failMessage) }
////            return _validator.get()
////        }
////    }
////}
////
////abstract class BaseDtoValidator {
//////    abstract fun validations() : List<Validation<out BaseDtoValidator>>
////    abstract fun toDto() : BaseDto
////
//////    inline fun fromContext(ctx : Context) : Either<Problems.Problem, BaseDto> = try {
//////        val result = validateAndGet(ctx)
//////        return result.to
//////    } catch (e : Exception) {
//////
//////    }
////
////    companion object {
////        fun validations() : List<Validation<out BaseDtoValidator>> = listOf()
////    }
////}
//
////abstract class BaseDto {
////    companion object {
////        inline fun <reified T : UnvalidatedBaseDto<T2>, T2 : BaseDto> fromContext(ctx: Context) : Either<Problems.Problem, BaseDto> {
////            return try {
////                val validator = ctx.bodyValidator<T>()
////                this.validations<T, T2>().forEach { validator.check(it.test, it.failMessage) }
////                val myObject = validator.get()
////                Either.Value(myObject.toDto())
////            } catch (e : Exception) {
////                var message = if (e.message?.startsWith("Couldn't deserialize") == true)
////                    "Unable to parse request body"
////                else
////                    e.localizedMessage.split(" - ").drop(1).joinToString()
////
////                Either.Problem(Problems.VALIDATION_ERROR(message))
////            }
////        }
////
////        open inline fun <reified T : UnvalidatedBaseDto<T2>, T2 : BaseDto> validations() = listOf<Validation<T, T2>>()
////    }
////}
////
////abstract class UnvalidatedBaseDto<out T : BaseDto> {
////    abstract fun toDto() : T
////}
////
//class Validation<T : IUnvalidatedDto>(val test : (T) -> Boolean, val failMessage : String)