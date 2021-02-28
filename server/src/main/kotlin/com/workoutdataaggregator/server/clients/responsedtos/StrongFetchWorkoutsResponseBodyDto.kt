package com.workoutdataaggregator.server.clients.responsedtos

import com.workoutdataaggregator.server.persistence.models.ExerciseModel
import com.workoutdataaggregator.server.persistence.models.ExerciseSetModel
import java.time.Instant
import java.util.*

data class StrongFetchWorkoutsResponseBodyDto(
    val results : List<StrongFetchWorkoutsResponseBodyResultDto>
) {
    fun toExerciseModelList() : List<ExerciseModel> = ExerciseModel.Factory().listFromDto(this)
}

data class StrongFetchWorkoutsResponseBodyResultDto(
    val objectId : String,
    val parseSetGroups : List<StrongWorkoutSetGroupDto>,
    val completionDate : StrongWorkoutDateDto?,
    val startDate : StrongWorkoutDateDto?,
    val notes : String,
    val name : String,
    val user : StrongWorkoutUserDto,
    val uniqueId : UUID,
    val createdAt : Date,
    val updatedAt : Date
)

data class StrongWorkoutDateDto(val iso: String) {
    fun toInstant(): Instant = Instant.parse(iso)
}

data class StrongWorkoutUserDto(val objectId: String)

data class StrongWorkoutSetGroupDto(
    val objectId : String,
    val parseSetsDictionary : List<StrongWorkoutSetDictionary>,
    val completionDate: StrongWorkoutDateDto?,
    val startDate: StrongWorkoutDateDto?,
    val superSetOrder: Int?,
    val notes: String,
    val parseExercise: StrongWorkoutExerciseDto,
    val uniqueId: UUID
) {
    fun toExerciseModel() = ExerciseModel.Factory().fromDto(this)

    fun estimatedExecutionTimeFor(strongWorkoutSetDictionary: StrongWorkoutSetDictionary): Instant {
        val length = completionDate!!.toInstant().epochSecond - startDate!!.toInstant().epochSecond
        val index = parseSetsDictionary.indexOf(strongWorkoutSetDictionary)
        val portion = (length / parseSetsDictionary.size - 1) * index

        return startDate.toInstant().plusSeconds(portion)
    }

    fun isInvalid() = completionDate == null || startDate == null
}

data class StrongWorkoutSetDictionary(
    val isPersonalRecord : Boolean,
    val reps : Int,
    val tagsValue : Int,
    val exerciseTypeValue: Int,
    val expectedReps: Int?,
    val isChecked: Boolean,
    val personalRecords: String?,
    val kilograms: Float?,
    val expectedKilograms: Float?
) {
    fun toExerciseSetModel(workoutId : UUID, estimatedExecutionTime : Instant) = ExerciseSetModel.Factory().fromDto(this, workoutId, estimatedExecutionTime)
}

data class StrongWorkoutExerciseDto(
    val objectId: String,
    val createdAt: Date,
    val updatedAt: Date,
    val name: String,
    val exerciseType: Int,
    val bodyParts: Int,
    val uniqueId: UUID,
    val instructions: String?
)