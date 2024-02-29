package peoplehub.di

import DbMapper
import DbMapperImpl
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import peoplehub.data.db.AppDatabase
import peoplehub.data.repository.PeopleRepository
import peoplehub.data.repository.PeopleRepositoryImpl
import peoplehub.ui.addPerson.AddPersonViewModel
import peoplehub.ui.people.PeopleViewModel
import peoplehub.ui.personDetails.PersonDetailsViewModel

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

    single<PeopleRepository> {
        PeopleRepositoryImpl(
            peopleDao = get(),
            dbMapper = get()
        )
    }

    single<DbMapper> {
        DbMapperImpl()
    }

    single<PeopleRepository> {
        PeopleRepositoryImpl(
            peopleDao = get(),
            dbMapper = get()
        )
    }

    viewModel { PeopleViewModel(peopleRepository = get()) }

    viewModel { (personId: Int) ->
        PersonDetailsViewModel(personId = personId, peopleRepository = get())
    }

    viewModel { AddPersonViewModel(peopleRepository = get()) }
}
