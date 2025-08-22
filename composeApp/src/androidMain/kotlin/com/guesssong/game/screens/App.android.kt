package com.guesssong.game.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
actual fun createGameViewModel(): GameViewModel {
    return viewModel()
} 