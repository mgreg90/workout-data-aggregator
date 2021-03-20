package com.workoutdataaggregator.server.persistence.repositories

import com.workoutdataaggregator.server.persistence.MongoDb
import com.workoutdataaggregator.server.persistence.daos.ExerciseDao
import com.workoutdataaggregator.server.persistence.daos.ExerciseSetDao
import com.workoutdataaggregator.server.persistence.models.ExerciseModel
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
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
                val dao = exerciseModel.toDao()
                collection.insertOne(dao)
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

    fun findWithinDates(startDate: Instant, endDate: Instant): Either<Problems.Problem, List<ExerciseModel>> {
        // TODO fix this query - currently returning too much data
        val query = and(
            ExerciseDao::sets / ExerciseSetDao::executedAt gt startDate,
            ExerciseDao::sets / ExerciseSetDao::executedAt lt endDate
        )
        val result = collection.find(query).toList().map { it.toModel() }
        return Either.Value(result)
    }

    companion object {
        const val DB_NAME = "exercises"
        val beginningOfTime: Instant = Instant.ofEpochSecond(1262304000) // Jan 1, 2010
    }
}