package com.workoutdataaggregator.server.persistence.daos

import com.workoutdataaggregator.server.persistence.models.IModel
import java.time.Instant

interface IDao {
    val createdAt : Instant?
    fun toModel() : IModel
}