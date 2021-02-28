package com.workoutdataaggregator.server.persistence.daos

import com.workoutdataaggregator.server.persistence.models.ExerciseBodyParts
import com.workoutdataaggregator.server.persistence.models.ExerciseModel
import com.workoutdataaggregator.server.persistence.models.ExerciseType
import java.util.*


class ExerciseDao(
        id : String,
        val strongId : UUID,
        val name : String,
        val exerciseType : ExerciseType,
        val exerciseBodyParts : ExerciseBodyParts,
        val instructions : String?,
        val sets : List<ExerciseSetDao>
) : BaseDao(id) {
    override fun toModel() = ExerciseModel.Factory().fromDao(this)
}