package com.workoutdataaggregator.server.persistence.repositories

object Repositories {
    var all = mutableListOf<IRepository>()
    lateinit var personRepository: PersonRepository

    fun init(): Repositories {
        personRepository = PersonRepository()

        all.add(personRepository)

        return this
    }

    fun registerIndexes() : Repositories {
        all.forEach { it.registerIndexes() }
        return this
    }
}