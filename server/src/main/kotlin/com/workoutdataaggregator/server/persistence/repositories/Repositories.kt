package com.workoutdataaggregator.server.persistence.repositories

object Repositories {
    var all = listOf<IRepository>()

    lateinit var personRepository: PersonRepository
    lateinit var exerciseRepository: ExerciseRepository

    fun init(): Repositories {
        personRepository = PersonRepository()
        exerciseRepository = ExerciseRepository()

        all = listOf(
                personRepository,
                exerciseRepository
        )

        return this
    }

    fun registerIndexes() : Repositories {
        all.forEach { it.registerIndexes() }
        return this
    }
}