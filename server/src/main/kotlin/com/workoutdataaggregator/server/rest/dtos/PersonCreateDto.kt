package com.workoutdataaggregator.server.rest.dtos

import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
import io.javalin.http.Context
import org.jetbrains.annotations.NotNull
import java.lang.Exception

class PersonCreateDto(val name : String, val age : Int) : DtoBase()

class UnvalidatedPersonCreateDto(val name: String?, val age : Int?) : UnvalidatedDtoBase() {
    override fun toDto(): IDtoBase = PersonCreateDto(
        name = name!!,
        age = age!!
    )
}

class PersonCreateDtoValidator(unvalidatedDto : UnvalidatedPersonCreateDto) : DtoValidatorBase(unvalidatedDto) {
    override val rules: List<Rule<IUnvalidatedDtoBase<IDtoBase>>> = listOf(
        Rule(
            logic = { unvalidatedDto.age != null && unvalidatedDto.age > 0 },
            field = "age",
            message = "age must be greater than 0"
        ),
        Rule(
            logic = { unvalidatedDto.age != null },
            field = "age",
            message = "age cannot be null"
        ),
        Rule(
            logic = { unvalidatedDto.name != null },
            field = "name",
            message = "name cannot be null"
        ),
        Rule(
            logic = { unvalidatedDto.name != null && unvalidatedDto.name.isNotEmpty() },
            field = "name",
            message = "name must be longer than 0 characters"
        )
    )
}
//
//class UnvalidatedPersonCreateDto(val name : String?, val age : Int) : IUnvalidatedDto {
//    override fun toDto() = PersonCreateDto(
//        name = name!!,
//        age = age!!
//    )
//}
//
//class PersonCreateDtoValidator : IDtoValidator {
////    override fun validateAndGetDto(ctx : Context) : Either<Problems.Problem, PersonCreateDto> {
////        return try {
////            val bodyValidator = ctx.bodyValidator<UnvalidatedPersonCreateDto>()
////            validations().forEach { bodyValidator.check(it.test, it.failMessage) }
////            Either.Value(bodyValidator.get().toDto())
////        } catch (e : Exception) {
////            var message = if (e.message?.startsWith("Couldn't deserialize") == true)
////                "Unable to parse request body"
////            else
////                e.localizedMessage.split(" - ").drop(1).joinToString()
////
////            Either.Problem(Problems.VALIDATION_ERROR(message))
////        }
////    }
//    override fun validations() = listOf(
//        Validation<UnvalidatedPersonCreateDto>({ it.name?.isNotEmpty() == true }, "name cannot be empty"),
//        Validation<UnvalidatedPersonCreateDto>({ it.age!! > 0 }, "age must be greater than zero"),
//        Validation<UnvalidatedPersonCreateDto>({ it.age != null}, "age cannot be empty")
//    )
//}
//

//
//class PersonCreateDtoValidator(
//    val name : String?,
//    val age : Int?
//) : BaseDtoValidator() {
//
//    override fun validations() = listOf<Validation<PersonCreateDtoValidator>>(
//        Validation({ it.name?.isNotEmpty() == true }, "name cannot be empty"),
//        Validation({ it.age!! > 0 }, "age must be greater than zero"),
//        Validation({ it.age != null}, "age cannot be empty")
//    )
//
//    override fun toDto() = PersonCreateDto(
//        name = name!!,
//        age = age!!
//    )
//
//}


//class UnvalidatedPersonCreateDto(val name: String?, val age: Int?) : UnvalidatedBaseDto<PersonCreateDto>() {
//    override fun toDto() = PersonCreateDto(
//        name = name!!,
//        age = age!!
//    )
//}
//
//data class PersonCreateDto(val name: String, val age: Int) : BaseDto() {
//    override fun validations() = listOf<Validation<UnvalidatedPersonCreateDto, PersonCreateDto>>(
//        Validation({ it.name?.isNotEmpty() == true }, "name cannot be empty"),
//        Validation({ it.age!! > 0 }, "age must be greater than zero"),
//        Validation({ it.age != null}, "age cannot be empty")
//    )
//}
