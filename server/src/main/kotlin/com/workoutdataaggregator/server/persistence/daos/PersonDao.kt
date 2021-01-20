package com.workoutdataaggregator.server.persistence.daos

import com.workoutdataaggregator.server.persistence.models.PersonModel

class PersonDao(
    id: String,
    val name: String,
    val age: Int
) : BaseDao(id) {
    override fun toModel() = PersonModel.Factory().fromDao(this)
}