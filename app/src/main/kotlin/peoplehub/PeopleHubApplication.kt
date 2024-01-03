package peoplehub

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import peoplehub.di.appModule

class PeopleHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PeopleHubApplication)
            modules(appModule)
        }
    }
}
