package com.workoutdataaggregator.server.persistence.models

import com.workoutdataaggregator.server.persistence.daos.PersonDao
import com.workoutdataaggregator.server.rest.dtos.PersonCreateDto
import com.workoutdataaggregator.server.rest.dtos.PersonUpdateDto
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

class PersonModel(
    id : UUID? = null,
    val name : String,
    val age : Int) : BaseModel<PersonDao>(id) {

        class Factory : BaseModel.Factory<PersonDao>() {
            fun fromCreateDto(createDto : PersonCreateDto) = PersonModel(
                name = createDto.name!!,
                age = createDto.age
            )

            fun fromUpdateDto(updateDto : PersonUpdateDto) = PersonModel(
                id = updateDto.id,
                name = updateDto.name,
                age = updateDto.age
            )

            override fun fromDao(dao : PersonDao) = PersonModel(
                id = UUID.fromString(dao.id),
                name = dao.name,
                age = dao.age
            )
        }

        override fun toDao() = PersonDao(
            id = currentOrNewId().toString(),
            name = this.name,
            age = this.age
        )
    }