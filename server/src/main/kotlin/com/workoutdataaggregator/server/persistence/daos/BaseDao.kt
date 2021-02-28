package com.workoutdataaggregator.server.persistence.daos

import org.bson.codecs.pojo.annotations.BsonId
import java.time.Instant

abstract class BaseDao(@BsonId val id: String) : IDao {
    override val createdAt: Instant = Instant.now()
}