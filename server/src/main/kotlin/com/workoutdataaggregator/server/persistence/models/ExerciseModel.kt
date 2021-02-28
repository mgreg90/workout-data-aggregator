package com.workoutdataaggregator.server.persistence.models

import com.workoutdataaggregator.server.clients.responsedtos.StrongFetchWorkoutsResponseBodyDto
import com.workoutdataaggregator.server.clients.responsedtos.StrongWorkoutSetGroupDto
import com.workoutdataaggregator.server.persistence.daos.ExerciseDao
import java.lang.Exception
import java.time.Instant
import java.util.*

class ExerciseModel(
    id : UUID? = null,
    val strongId : UUID,
    val name : String,
    val exerciseType : ExerciseType,
    val exerciseBodyParts : ExerciseBodyParts,
    val instructions : String?,
    val sets : MutableList<ExerciseSetModel>,
    override val createdAt : Instant? = null
) : BaseModel<ExerciseDao>(id){

    class Factory : BaseModel.Factory<ExerciseDao>() {
        override fun fromDao(dao : ExerciseDao) = ExerciseModel(
            id = UUID.fromString(dao.id),
            strongId = dao.strongId,
            name = dao.name,
            exerciseType = dao.exerciseType,
            exerciseBodyParts = dao.exerciseBodyParts,
            instructions = dao.instructions,
            sets = dao.sets.map { it.toModel() }.toMutableList(),
            createdAt = dao.createdAt
        )

        fun fromDto(dto : StrongWorkoutSetGroupDto) = ExerciseModel(
            strongId = dto.parseExercise.uniqueId,
            name = dto.parseExercise.name,
            exerciseType = ExerciseType.from(dto.parseExercise.exerciseType),
            exerciseBodyParts = ExerciseBodyParts.from(dto.parseExercise.bodyParts),
            instructions = dto.parseExercise.instructions,
            sets = dto.parseSetsDictionary.map { set ->
                if (dto.isInvalid()) return@map null
                set.toExerciseSetModel(dto.uniqueId, dto.estimatedExecutionTimeFor(set))
            }.filterNotNull().toMutableList()
        )

        fun listFromDto(dto : StrongFetchWorkoutsResponseBodyDto) : List<ExerciseModel> {
            var exercises = mutableListOf<ExerciseModel>()

            dto.results.forEach { workout ->
                workout.parseSetGroups.forEach { exercise ->
                    var exerciseModel = exercises.find { it.strongId == exercise.parseExercise.uniqueId }

                    if (exerciseModel == null) {
                        exerciseModel = exercise.toExerciseModel()
                        exercises.add(exerciseModel)
                    } else {
                        exerciseModel.addSetsFrom(exercise.toExerciseModel())
                    }

                }
            }

            return exercises
        }
    }

    private fun addSetsFrom(otherExerciseModel : ExerciseModel) {
        val existingWorkoutIds = sets.map { it.workoutId }

        otherExerciseModel.sets.forEach {
            if(!existingWorkoutIds.contains(it.workoutId)) sets.add(it)
        }
    }

    override fun toDao() = ExerciseDao(
        id = currentOrNewId().toString(),
        strongId = strongId,
        name = name,
        exerciseType = exerciseType,
        exerciseBodyParts = exerciseBodyParts,
        instructions = instructions,
        sets = sets.map { it.toDao() }
    )
}