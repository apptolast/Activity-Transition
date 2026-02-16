package io.rndev.paparcar.di

import io.rndev.paparcar.data.repository.LocationSenderRepositoryImpl
import io.rndev.paparcar.domain.repository.LocationSenderRepository
import io.rndev.paparcar.domain.usecase.DrivingStateEvaluator
import io.rndev.paparcar.domain.usecase.ObserveDrivingStateUseCase
import io.rndev.paparcar.domain.usecase.SendLocationIfNeededUseCase
import io.rndev.paparcar.presentation.viewmodel.DrivingViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val commonModule = module {

    // Repositories (Common)
    // LocationSenderRepositoryImpl is in commonMain, so it's defined here.
    single<LocationSenderRepository> { LocationSenderRepositoryImpl() }

    // Domain (UseCases and Evaluators)
    factoryOf(::DrivingStateEvaluator)
    factoryOf(::ObserveDrivingStateUseCase)
    factoryOf(::SendLocationIfNeededUseCase)

    // Presentation (ViewModels)
    // We use factoryOf for ViewModels in commonMain to keep it platform-agnostic.
    factoryOf(::DrivingViewModel)
}
