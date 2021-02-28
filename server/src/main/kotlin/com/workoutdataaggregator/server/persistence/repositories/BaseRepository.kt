package com.workoutdataaggregator.server.persistence.repositories

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.workoutdataaggregator.server.persistence.daos.BaseDao
import com.workoutdataaggregator.server.persistence.models.BaseModel
import com.workoutdataaggregator.server.utils.Either
import com.workoutdataaggregator.server.utils.Problems
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.updateOne
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.reflect.KProperty1

abstract class BaseRepository<TModel : BaseModel<TDao>, TDao : BaseDao> : IRepository {
    protected val logger = LoggerFactory.getLogger(javaClass)
    protected abstract val collection : MongoCollection<TDao>
    protected abstract val idField : KProperty1<TDao, String>

    fun findById(id : UUID) : Either<Problems.Problem, TModel> =
        when (val result = collection.findOne(idField eq id.toString())?.toModel()) {
            null -> Either.Problem(Problems.NOT_FOUND("$javaClass not found for id: $id"))
            else -> Either.Value(result as TModel)
    }

    fun findAll() = collection.find().toList().map { it.toModel() }

    fun createOne(model: TModel) : Either<Problems.Problem, TModel> {
        return try {
            val dao = model.toDao()
            collection.insertOne(dao)
            Either.Value(dao.toModel() as TModel)
        } catch (e: MongoException) {
            logger.error(e.message)
            Either.Problem(Problems.DATABASE_ACTION_FAILED_ERROR("Write to database failed!"))
        }
    }

    fun updateOne(model: TModel) : Either<Problems.Problem, TModel> {
        val dao = model.toDao()
        val result = collection.updateOne(idField eq dao.id)

        if (result.matchedCount > 0) return Either.Value(dao.toModel() as TModel)

        logger.error("Database update failed - ${model.id}")
        return Either.Problem(Problems.DATABASE_ACTION_FAILED_ERROR("Update to database failed!"))
    }

    fun deleteOne(id: UUID) : Either<Problems.Problem, Boolean>{
        val result = collection.deleteOne(idField eq id.toString())
        if (result.wasAcknowledged()) return Either.Value(true)

        logger.error("Database delete failed - $id")
        return Either.Problem(Problems.DATABASE_ACTION_FAILED_ERROR("Delete to database failed!"))
    }
}