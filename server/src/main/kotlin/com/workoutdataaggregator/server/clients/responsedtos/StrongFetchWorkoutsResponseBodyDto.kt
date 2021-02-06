package com.workoutdataaggregator.server.clients.responsedtos

import java.util.*

data class StrongFetchWorkoutsResponseBodyDto(
    val results : List<StrongFetchWorkoutsResponseBodyResultDto>
)

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

data class StrongWorkoutDateDto(val iso: Date)

data class StrongWorkoutUserDto(val objectId: String)

data class StrongWorkoutSetGroupDto(
    val objectId : String,
    val parseSetsDictionary : List<StrongWorkoutSetDictionary>,
    val completionDate: StrongWorkoutDateDto?,
    val startDate: StrongWorkoutDateDto?,
    val superSetOrder: Int?,
    val notes: String,
    val parseExercise: StrongWorkoutExerciseDto
)

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
)

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