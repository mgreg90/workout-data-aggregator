package com.workoutdataaggregator.server.persistence.models

import java.time.Instant

interface IModel {
    val createdAt : Instant?
}