package peoplehub.data.repository

import kotlinx.coroutines.flow.Flow
import peoplehub.data.db.dao.PeopleDao
import peoplehub.domain.model.Person

internal class PeopleRepositoryImpl(
    private val peopleDao: PeopleDao
) : PeopleRepository {
    override fun getPeople(): Flow<List<Person>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertPerson(person: Person) {
        TODO("Not yet implemented")
    }
}
