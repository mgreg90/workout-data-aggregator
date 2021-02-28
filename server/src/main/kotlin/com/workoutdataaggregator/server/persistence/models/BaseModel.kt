package com.workoutdataaggregator.server.persistence.models

import com.workoutdataaggregator.server.persistence.daos.BaseDao
import java.time.Instant
import java.util.*

abstract class BaseModel<T : BaseDao>(val id: UUID? = null) : IModel {
    abstract fun toDao() : T
    fun currentOrNewId() : UUID = this.id ?: UUID.randomUUID()
    abstract class Factory<T> {
        abstract fun fromDao(dao : T) : IModel
    }
}