package com.workoutdataaggregator.server.persistence

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.bson.UuidRepresentation
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.litote.kmongo.runCommand
import java.lang.Exception
import java.util.concurrent.TimeUnit

object MongoDb {
    private const val DB_NAME = "workout-data-aggregator"
    private const val SERVER_SELECTION_TIMEOUT_MS = 3000L
    private const val CONNECT_TIMEOUT_MS = 10000

    lateinit var client: MongoClient
    lateinit var database: MongoDatabase

    fun init() {
        client = KMongo.createClient(mongoClientSettings())
        database = client.getDatabase(DB_NAME)
    }

    fun isLive() = try {
        database.runCommand<Document>("{\"ping\": 1}")["ok"]
        true
    } catch (e: Exception) {
        false
    }

    private fun mongoClientSettings() = MongoClientSettings.builder()
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applyToClusterSettings { it.serverSelectionTimeout(SERVER_SELECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)}
        .applyToSocketSettings { it.connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS) }
        .build()

    inline fun <reified T : Any> getCollection() = database.getCollection<T>()
    inline fun <reified T : Any> getCollection(dbName: String) = database.getCollection<T>(dbName)

}