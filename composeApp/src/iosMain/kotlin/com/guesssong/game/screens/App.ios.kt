package com.guesssong.game.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun createGameViewModel(): GameViewModel {
    return remember { GameViewModel() }
} 