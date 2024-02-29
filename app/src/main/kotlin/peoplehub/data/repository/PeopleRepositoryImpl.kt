package peoplehub.data.repository

import DbMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import peoplehub.data.db.dao.PeopleDao
import peoplehub.domain.model.Person

internal class PeopleRepositoryImpl(
    private val peopleDao: PeopleDao,
    private val dbMapper: DbMapper
) : PeopleRepository {
    override fun getPeople(): Flow<List<Person>> =
        peopleDao.getAll().map { it.map(dbMapper::mapToPerson) }

    override suspend fun insertPerson(person: Person) {
        peopleDao.insertPerson(dbMapper.mapToPersonEntity(person))
    }
}
