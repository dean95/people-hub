package peoplehub.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import peoplehub.data.db.model.PersonEntity

@Dao
interface PeopleDao {
    @Query("SELECT * FROM person")
    fun getAll(): Flow<List<PersonEntity>>
}
