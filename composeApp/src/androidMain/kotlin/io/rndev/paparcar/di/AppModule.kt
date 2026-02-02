package io.rndev.paparcar.di

import io.rndev.paparcar.data.mapper.ActivityTypeMapper
import io.rndev.paparcar.data.repository.ActivityRepositoryImpl
import io.rndev.paparcar.data.repository.LocationRepositoryImpl
import io.rndev.paparcar.data.repository.LocationSenderRepositoryImpl
import io.rndev.paparcar.data.source.ActivityRecognitionDataSource
import io.rndev.paparcar.data.source.ActivityRecognitionDataSourceImpl
import io.rndev.paparcar.data.source.LocationDataSource
import io.rndev.paparcar.data.source.LocationDataSourceImpl
import io.rndev.paparcar.domain.repository.ActivityRepository
import io.rndev.paparcar.domain.repository.LocationRepository
import io.rndev.paparcar.domain.repository.LocationSenderRepository
import io.rndev.paparcar.domain.usecase.DrivingStateEvaluator
import io.rndev.paparcar.domain.usecase.ObserveDrivingStateUseCase
import io.rndev.paparcar.domain.usecase.SendLocationIfNeededUseCase
import io.rndev.paparcar.presentation.DrivingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    // Data
    single<ActivityRecognitionDataSource> { ActivityRecognitionDataSourceImpl(androidContext(), get()) }
    single<LocationDataSource> { LocationDataSourceImpl(androidContext()) }
    single<ActivityRepository> { ActivityRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    single<LocationSenderRepository> { LocationSenderRepositoryImpl() }

    // Mappers
    factory { ActivityTypeMapper() }

    // Domain
    factory { DrivingStateEvaluator() }
    factory { ObserveDrivingStateUseCase(get(), get(), get()) }
    factory { SendLocationIfNeededUseCase(get()) }

    // Presentation
    viewModelOf(::DrivingViewModel)
}
