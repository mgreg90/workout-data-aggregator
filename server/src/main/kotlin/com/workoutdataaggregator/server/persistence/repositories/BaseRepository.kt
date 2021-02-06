package com.workoutdataaggregator.server.persistence.repositories

import com.mongodb.MongoException
import com.mongodb.client.MongoCollection
import com.workoutdataaggregator.server.persistence.daos.BaseDao
import com.workoutdataaggregator.server.persistence.models.BaseModel
import com.workoutdataaggregator.server.persistence.models.IModel
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.updateOne
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.reflect.KProperty1

abstract class BaseRepository<TModel : BaseModel<TDao>, TDao : BaseDao> : IRepository {
    protected val logger = LoggerFactory.getLogger(javaClass)
    abstract val collection : MongoCollection<TDao>
    abstract val idField : KProperty1<TDao, String>

    fun findById(id : UUID) = collection.findOne(idField eq id.toString())?.toModel()

    fun findAll() = collection.find().toList().map { it.toModel() }

    fun createOne(model: TModel) : IModel? = try {
        val dao = model.toDao()
        collection.insertOne(dao)
        dao.toModel()
    } catch (e : MongoException) {
        logger.error(e.message)
        null
    }

    fun updateOne(model: TModel) : IModel {
        val dao = model.toDao()
        collection.updateOne(idField eq dao.id)
        return dao.toModel()
    }

    fun deleteOne(id: UUID) = collection.deleteOne(idField eq id.toString())
}