package com.workoutdataaggregator.server.persistence.daos

import com.workoutdataaggregator.server.persistence.models.ExerciseSetModel
import java.time.Instant

class ExerciseSetDao(
        id : String,
        val isPersonalRecord : Boolean,
        val reps : Int,
        val expectedReps : Int?,
        val weightKg : Long?,
        val expectedWeightKg : Long?,
        val workoutId : String,
        val executedAt : Instant
) : BaseDao(id) {
    override fun toModel() = ExerciseSetModel.Factory().fromDao(this)
}