package io.rndev.paparcar

import android.app.Application
import io.rndev.paparcar.di.androidPlatformModule
import io.rndev.paparcar.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PaparcarApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PaparcarApplication)
            modules(commonModule, androidPlatformModule)
        }
    }
}
