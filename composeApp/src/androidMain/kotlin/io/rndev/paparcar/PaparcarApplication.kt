package io.rndev.paparcar

import android.app.Application
import io.rndev.paparcar.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PaparcarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PaparcarApplication)
            modules(appModule)
        }
    }
}