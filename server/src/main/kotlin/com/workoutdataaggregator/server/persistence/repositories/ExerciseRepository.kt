package com.workoutdataaggregator.server.persistence.repositories

import com.workoutdataaggregator.server.persistence.MongoDb
import com.workoutdataaggregator.server.persistence.daos.ExerciseDao
import com.workoutdataaggregator.server.persistence.models.ExerciseModel
import org.litote.kmongo.*
import java.time.Instant
import java.util.*

class ExerciseRepository : IRepository, BaseRepository<ExerciseModel, ExerciseDao>() {
    override val idField = ExerciseDao::id
    override val collection = MongoDb.getCollection<ExerciseDao>(DB_NAME)

    override fun registerIndexes() {
        collection.createIndex(ascendingIndex(ExerciseDao::id))
    }

    fun createOrUpdateMany(exercises : List<ExerciseModel>) {
        exercises.forEach { exerciseModel ->
            val existingExercise = collection.find(ExerciseDao::strongId eq exerciseModel.strongId).firstOrNull()

            if (existingExercise == null) {
                collection.insertOne(exerciseModel.toDao())
                return@forEach
            }

            exerciseModel.sets.forEach { exerciseSetModel ->
                if (!existingExercise.sets.any { UUID.fromString(it.workoutId) == exerciseSetModel.workoutId })
                    exerciseModel.sets.add(exerciseSetModel)
            }
            val dao = exerciseModel.toDao()
            collection.updateOne(ExerciseDao::id eq dao.id, dao)
        }
    }

    fun getLastUpdateTime(): Instant =
        collection.find().descendingSort(ExerciseDao::createdAt).limit(1).firstOrNull()?.createdAt ?: beginningOfTime

    companion object {
        const val DB_NAME = "exercises"
        val beginningOfTime: Instant = Instant.ofEpochSecond(1262304000) // Jan 1, 2010
    }
}