package ncshack.samba.mizan

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ncshack.samba.mizan.di.appModule

class LexifyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@LexifyApp)
            modules(appModule)
        }
    }
}
