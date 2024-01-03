package peoplehub.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import peoplehub.data.db.dao.PeopleDao
import peoplehub.data.db.model.AddressEntity
import peoplehub.data.db.model.PersonEntity

@Database(entities = [PersonEntity::class, AddressEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun peopleDao(): PeopleDao
}
