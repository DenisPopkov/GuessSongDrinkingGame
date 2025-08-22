package com.guesssong.game.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.guesssong.game.domain.GameScreen
import com.guesssong.game.theme.LocalColorSystem
import com.guesssong.game.theme.LocalSpacingSystem
import com.guesssong.game.theme.SpacingSystem
import com.guesssong.game.theme.getColorSystem

@Composable
fun App() {
    val colorSystem = getColorSystem()
    val spacingSystem = SpacingSystem()

    val viewModel: GameViewModel = createGameViewModel()
    val gameState by viewModel.gameState.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(gameState.snackbarMessage) {
        println("App: LaunchedEffect triggered, snackbarMessage: ${gameState.snackbarMessage}")
        gameState.snackbarMessage?.let { message ->
            println("App: Showing snackbar with message: $message")
            try {
                snackbarHostState.showSnackbar(message)
                println("App: Snackbar shown successfully")
                viewModel.hideSnackbar()
                println("App: Snackbar hidden")
            } catch (e: Exception) {
                println("App: Error showing snackbar: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    CompositionLocalProvider(
        LocalColorSystem provides colorSystem,
        LocalSpacingSystem provides spacingSystem
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorSystem.bg)
        ) {
            when (currentScreen) {
                is GameScreen.PlayerSelection -> {
                    PlayerSelectionScreen(
                        playerCount = gameState.playerCount,
                        onIncrement = { viewModel.incrementPlayerCount() },
                        onDecrement = { viewModel.decrementPlayerCount() },
                        onStartGame = { viewModel.startGame() }
                    )
                }

                is GameScreen.Loading -> {
                    LoadingScreen(
                        loadingText = gameState.loadingProgress
                    )
                }

                is GameScreen.CategorySelection -> {
                    CategorySelectionScreen(
                        availableCategories = gameState.availableCategories,
                        onCategorySelected = { category -> viewModel.selectCategory(category) }
                    )
                }

                is GameScreen.SongPlaying -> {
                    gameState.currentSong?.let { song ->
                        SongPlayingScreen(
                            song = song,
                            isPlaying = gameState.isPlaying,
                            showAnswer = gameState.showAnswer,
                            onShowAnswer = { viewModel.showAnswer() },
                            onTestSystemSound = { viewModel.testSystemSound() }
                        )
                    }
                }

                is GameScreen.GameFinished -> {
                    GameFinishedScreen(
                        onRestartGame = { viewModel.restartGame() }
                    )
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
expect fun createGameViewModel(): GameViewModel
