package com.guesssong.game.di

import com.guesssong.game.screens.GameViewModel
import com.guesssong.game.service.AudioService
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val serviceModule = module {
    single { AudioService() }
}

val viewModelModule = module {
    viewModel { GameViewModel() }
}

fun initKoin() {
    startKoin {
        modules(serviceModule, viewModelModule)
    }
}
