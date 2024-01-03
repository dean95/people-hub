package peoplehub.data.repository

import kotlinx.coroutines.flow.Flow
import peoplehub.domain.model.Person

interface PeopleRepository {

    fun getPeople(): Flow<List<Person>>

    suspend fun insertPerson(person: Person)
}
