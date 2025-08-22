package com.guesssong.game.domain

import guesssongdrinkinggame.composeapp.generated.resources.Res
import guesssongdrinkinggame.composeapp.generated.resources.ic_english
import guesssongdrinkinggame.composeapp.generated.resources.ic_pop
import guesssongdrinkinggame.composeapp.generated.resources.ic_rap
import guesssongdrinkinggame.composeapp.generated.resources.ic_russian
import org.jetbrains.compose.resources.DrawableResource

enum class Category(val displayName: String, val imageResource: DrawableResource) {
    RAP("Рэп", Res.drawable.ic_rap),
    TWO_THOUSAND_TWENTIES("2020-е", Res.drawable.ic_pop),
    FOREIGN("Зарубежное", Res.drawable.ic_english),
    RUSSIAN("Русское", Res.drawable.ic_russian)
}

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val previewUrl: String,
    val category: String
)

data class GameState(
    val playerCount: Int = 3,
    val selectedCategory: Category? = null,
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val showAnswer: Boolean = false,
    val isLoadingCategory: Boolean = false,
    val gameFinished: Boolean = false,
    val availableCategories: List<Category> = Category.entries,
    val songsByCategory: Map<Category, List<Song>> = emptyMap(),
    val snackbarMessage: String? = null,
    val isLoadingSongs: Boolean = false,
    val loadingProgress: String = ""
)

sealed class GameScreen {
    object PlayerSelection : GameScreen()
    object Loading : GameScreen()
    object CategorySelection : GameScreen()
    object SongPlaying : GameScreen()
    object GameFinished : GameScreen()
} 