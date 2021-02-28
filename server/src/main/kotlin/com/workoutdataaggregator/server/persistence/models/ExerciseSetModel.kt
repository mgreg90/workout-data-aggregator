package com.workoutdataaggregator.server.persistence.models

import com.workoutdataaggregator.server.clients.responsedtos.StrongWorkoutSetDictionary
import com.workoutdataaggregator.server.persistence.daos.ExerciseSetDao
import java.time.Instant
import java.util.*

class ExerciseSetModel(
    id : UUID? = null,
    val isPersonalRecord : Boolean,
    val reps : Int,
    val expectedReps : Int?,
    val weightKg : Long?,
    val expectedWeightKg : Long?,
    val workoutId : UUID,
    val executedAt : Instant,
    override val createdAt : Instant? = null
) : BaseModel<ExerciseSetDao>(id) {

    class Factory : BaseModel.Factory<ExerciseSetDao>() {
        override fun fromDao(dao : ExerciseSetDao) = ExerciseSetModel(
            id = UUID.fromString(dao.id),
            isPersonalRecord = dao.isPersonalRecord,
            reps = dao.reps,
            expectedReps = dao.expectedReps,
            weightKg = dao.weightKg,
            expectedWeightKg = dao.expectedWeightKg,
            workoutId = UUID.fromString(dao.workoutId),
            executedAt = dao.executedAt
        )

        fun fromDto(
                dto : StrongWorkoutSetDictionary,
                workoutId : UUID,
                estimatedExecutionTime : Instant
        ) : ExerciseSetModel = ExerciseSetModel(
            isPersonalRecord = dto.isPersonalRecord,
            reps = dto.reps,
            expectedReps = dto.expectedReps,
            weightKg = dto.kilograms?.toLong(),
            expectedWeightKg = dto.expectedKilograms?.toLong(),
            workoutId = workoutId,
            executedAt = estimatedExecutionTime
        )
    }

    override fun toDao() = ExerciseSetDao(
        id = currentOrNewId().toString(),
        isPersonalRecord = isPersonalRecord,
        reps = reps,
        expectedReps = expectedReps,
        weightKg = weightKg,
        expectedWeightKg = expectedWeightKg,
        workoutId = workoutId.toString(),
        executedAt = executedAt
    )
}

enum class ExerciseType(val value : Int) {
    BARBELL(0),
    DUMBBELL(1),
    MACHINE_OTHER(2),
    WEIGHTED_BODYWEIGHT(3),
    ASSISTED_BODYWEIGHT(4),
    REPS_ONLY(5),
    CARDIO(6),
    DURATION(7),
    BAD_VALUE(8);

    companion object {
        fun from(value : Int) = values().firstOrNull { it.value == value } ?: BAD_VALUE
    }
}

enum class ExerciseBodyParts(val value : Int) {
    NONE(0),
    CORE(1),
    ARMS(2),
    BACK(3),
    CHEST(4),
    LEGS(5),
    SHOULDERS(6),
    OTHER(7),
    OLYMPIC(8),
    FULL_BODY(9),
    CARDIO(10),
    BAD_VALUE(11);

    companion object {
        fun from(value : Int) = values().firstOrNull { it.value == value } ?: BAD_VALUE
    }
}