package com.workoutdataaggregator.server.rest.dtos

import java.util.*

data class PersonUpdateDto(
    val id: UUID,
    val name: String,
    val age: Int
)