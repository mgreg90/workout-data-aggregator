package com.workoutdataaggregator.server.persistence.daos

import org.bson.codecs.pojo.annotations.BsonId

abstract class BaseDao(@BsonId val id: String) : IDao