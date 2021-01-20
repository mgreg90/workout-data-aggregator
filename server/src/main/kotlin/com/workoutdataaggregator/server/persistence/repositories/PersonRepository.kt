package com.workoutdataaggregator.server.persistence.repositories

import com.workoutdataaggregator.server.persistence.MongoDb
import com.workoutdataaggregator.server.persistence.daos.PersonDao
import com.workoutdataaggregator.server.persistence.models.PersonModel
import org.litote.kmongo.*
import org.litote.kmongo.index
import java.util.*

class PersonRepository : IRepository, BaseRepository<PersonModel, PersonDao>() {
    override val idField = PersonDao::id
    override val collection = MongoDb.getCollection<PersonDao>(DB_NAME)

    override fun registerIndexes() {
        collection.createIndex(ascendingIndex(PersonDao::id))
    }

    companion object {
        const val DB_NAME = "persons"
    }
}