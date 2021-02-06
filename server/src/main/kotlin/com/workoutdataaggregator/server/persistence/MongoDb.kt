package com.workoutdataaggregator.server.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.id.jackson.IdJacksonModule

object MongoDb {
    private const val DB_NAME = "workout-data-aggregator"

    lateinit var client: MongoClient
    lateinit var database: MongoDatabase

    fun init() {
        val objectMapper = ObjectMapper()
        objectMapper.registerModule(IdJacksonModule())

        client = KMongo.createClient(mongoClientSettings())
        database = client.getDatabase(DB_NAME)
    }

    private fun mongoClientSettings() = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .build()

    inline fun <reified T : Any> getCollection() = database.getCollection<T>()
    inline fun <reified T : Any> getCollection(persons: String) = database.getCollection<T>(persons)

}