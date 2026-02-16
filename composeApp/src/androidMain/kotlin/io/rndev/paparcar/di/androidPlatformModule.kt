package io.rndev.paparcar.di

import io.rndev.paparcar.data.mapper.ActivityTypeMapper
import io.rndev.paparcar.data.mapper.LocationMapper
import io.rndev.paparcar.data.repository.ActivityRepositoryImpl
import io.rndev.paparcar.data.repository.LocationRepositoryImpl
import io.rndev.paparcar.data.service.AndroidActivityRecognitionService
import io.rndev.paparcar.data.service.AndroidAppNotificationManager
import io.rndev.paparcar.data.service.AndroidLocationService
import io.rndev.paparcar.domain.repository.ActivityRepository
import io.rndev.paparcar.domain.repository.LocationRepository
import io.rndev.paparcar.domain.service.ActivityRecognitionService
import io.rndev.paparcar.domain.service.AppNotificationManager
import io.rndev.paparcar.domain.service.LocationService
import io.rndev.paparcar.platform.model.PermissionEventSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidPlatformModule = module {

    // Platform-specific Repositories
    single<ActivityRepository> { ActivityRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }

    // Platform-specific Mappers
    factory { ActivityTypeMapper() }
    factory { LocationMapper() }

    // Platform-specific Event Sources
    single { PermissionEventSource() }

    // Platform-specific Service Implementations
    single<ActivityRecognitionService> {
        AndroidActivityRecognitionService(
            androidContext(),
            get(),
        )
    }
    single<LocationService> { AndroidLocationService(androidContext(), get()) }
    single<AppNotificationManager> { AndroidAppNotificationManager(androidContext()) }
}
