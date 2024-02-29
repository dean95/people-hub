package peoplehub.data.repository

import kotlinx.coroutines.flow.Flow
import peoplehub.domain.model.Person

interface PeopleRepository {

    fun getPeople(): Flow<List<Person>>

    fun getPerson(id: String): Flow<Person>

    suspend fun insertPerson(person: Person)
}
