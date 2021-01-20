package com.workoutdataaggregator.server.persistence.daos

import com.workoutdataaggregator.server.persistence.models.IModel

interface IDao {
    fun toModel() : IModel
}