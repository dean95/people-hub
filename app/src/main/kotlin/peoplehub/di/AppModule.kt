package peoplehub.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import peoplehub.data.db.AppDatabase
import peoplehub.data.repository.PeopleRepository
import peoplehub.data.repository.PeopleRepositoryImpl

private const val DB_NAME = "app-database"

val appModule = module {

    single {
        Room.databaseBuilder(
            context = androidApplication(),
            klass = AppDatabase::class.java,
            name = DB_NAME
        ).build()
    }

    single {
        get<AppDatabase>().peopleDao()
    }

    single<PeopleRepository> { PeopleRepositoryImpl(peopleDao = get()) }
}
