package com.guesssong.game.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guesssong.game.data.DeezerSongDownloadService
import com.guesssong.game.domain.Category
import com.guesssong.game.domain.GameScreen
import com.guesssong.game.domain.GameState
import com.guesssong.game.domain.Song
import com.guesssong.game.service.AudioService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameViewModel : ViewModel(), KoinComponent {
    private val _gameState = MutableStateFlow(GameState())
    val gameState = _gameState.asStateFlow()

    private val _currentScreen = MutableStateFlow<GameScreen>(GameScreen.PlayerSelection)
    val currentScreen = _currentScreen.asStateFlow()

    private val audioService: AudioService by inject()
    private val deezerSongDownloadService = DeezerSongDownloadService()

        init {
        _gameState.value = _gameState.value.copy(songsByCategory = emptyMap())
    }



    fun incrementPlayerCount() {
        val currentCount = _gameState.value.playerCount
        if (currentCount < 9) {
            _gameState.value = _gameState.value.copy(playerCount = currentCount + 1)
        }
    }

    fun decrementPlayerCount() {
        val currentCount = _gameState.value.playerCount
        if (currentCount > 3) {
            _gameState.value = _gameState.value.copy(playerCount = currentCount - 1)
        }
    }

    fun startGame() {
        _currentScreen.value = GameScreen.Loading
        // Пробуем загрузить с Deezer, если не получится - используем локальные данные
        loadAllSongs()
    }
    
    private fun loadAllSongs() {
        viewModelScope.launch {
            val playerCount = _gameState.value.playerCount
            val tracksPerCategory = 2 // Ограничиваем количество треков
            
            _gameState.value = _gameState.value.copy(
                isLoadingSongs = true,
                loadingProgress = "Подключение к Deezer..."
            )
            
            println("GameViewModel: Using Deezer API for music search...")
            
            val categories = Category.values()
            val totalCategories = categories.size
            val songsByCategory = mutableMapOf<Category, List<Song>>()
            
            var successCount = 0
            
            for ((index, category) in categories.withIndex()) {
                try {
                    _gameState.value = _gameState.value.copy(
                        loadingProgress = "Загрузка ${category.displayName}... (${index + 1}/$totalCategories)"
                    )
                    
                    println("GameViewModel: Loading songs for category ${category.displayName}")
                    
                    // Загружаем треки для текущей категории
                    val songs = deezerSongDownloadService.downloadSongs(category.displayName)
                    
                    if (songs.isNotEmpty()) {
                        // Сохраняем результат и обновляем состояние
                        songsByCategory[category] = songs
                        _gameState.value = _gameState.value.copy(songsByCategory = songsByCategory.toMap())
                        successCount++
                        
                        println("GameViewModel: Successfully loaded ${songs.size} songs for ${category.displayName}")
                    } else {
                        println("GameViewModel: No songs loaded for ${category.displayName}, using fallback")
                    }
                    
                    // Пауза для освобождения памяти
                    kotlinx.coroutines.delay(300)
                    
                } catch (e: Exception) {
                    println("GameViewModel: Error loading songs for ${category.displayName}: ${e.message}")
                    e.printStackTrace()
                    
                    // Продолжаем с другими категориями
                    continue
                }
            }
            
            // Если загрузили меньше 2 категорий, используем локальные данные
            if (successCount < 2) {
                println("GameViewModel: Not enough categories loaded ($successCount), using local test data")
                createLocalTestData()
            }
            
            _gameState.value = _gameState.value.copy(
                isLoadingSongs = false,
                loadingProgress = ""
            )
            
            println("GameViewModel: loadAllSongs completed")
            println("GameViewModel: songsByCategory size: ${_gameState.value.songsByCategory.size}")
            
            _currentScreen.value = GameScreen.CategorySelection
        }
    }
    
    private fun createLocalTestData() {
        val testSongs = mutableMapOf<Category, List<Song>>()
        
        // Создаем тестовые песни для каждой категории
        testSongs[Category.RAP] = listOf(
            Song("1", "God's Plan", "Drake", "", "Рэп"),
            Song("2", "HUMBLE", "Kendrick Lamar", "", "Рэп"),
            Song("3", "SICKO MODE", "Travis Scott", "", "Рэп")
        )
        
        testSongs[Category.TWO_THOUSAND_TWENTIES] = listOf(
            Song("4", "drivers license", "Olivia Rodrigo", "", "2020-е"),
            Song("5", "MONTERO", "Lil Nas X", "", "2020-е"),
            Song("6", "Blinding Lights", "The Weeknd", "", "2020-е")
        )
        
        testSongs[Category.FOREIGN] = listOf(
            Song("7", "Shape of You", "Ed Sheeran", "", "Зарубежное"),
            Song("8", "Uptown Funk", "Bruno Mars", "", "Зарубежное"),
            Song("9", "Rolling in the Deep", "Adele", "", "Зарубежное")
        )
        
        testSongs[Category.RUSSIAN] = listOf(
            Song("10", "Владивосток 2000", "Мумий Тролль", "", "Русское"),
            Song("11", "Где же ты", "Тимати", "", "Русское"),
            Song("12", "Ариведерчи", "Земфира", "", "Русское")
        )
        
        _gameState.value = _gameState.value.copy(songsByCategory = testSongs)
        println("GameViewModel: Created local test data for all categories")
    }

    fun selectCategory(category: Category) {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoadingCategory = true)
            
            // Используем только локальные тестовые данные
            val availableSongs = _gameState.value.songsByCategory[category] ?: emptyList()
            
            _gameState.value = _gameState.value.copy(isLoadingCategory = false)
            
            println("GameViewModel: selectCategory called for ${category.displayName}")
            println("GameViewModel: availableSongs size: ${availableSongs.size}")
            
            if (availableSongs.isNotEmpty()) {
                val randomSong = availableSongs.random()
                val updatedSongs = _gameState.value.songsByCategory.toMutableMap()
                updatedSongs[category] = availableSongs.filter { it != randomSong }

                _gameState.value = _gameState.value.copy(
                    selectedCategory = category,
                    currentSong = randomSong,
                    isPlaying = true,
                    songsByCategory = updatedSongs
                )

                _currentScreen.value = GameScreen.SongPlaying
                startSongPlayback()
            } else {
                println("GameViewModel: No songs available for category ${category.displayName}")
                // Show error message or handle empty category
                _gameState.value = _gameState.value.copy(
                    snackbarMessage = "Нет песен доступно для категории ${category.displayName}"
                )
            }
        }
    }

    private fun startSongPlayback() {
        viewModelScope.launch {
            val currentSong = _gameState.value.currentSong
            if (currentSong != null) {
                println("GameViewModel: Starting playback for '${currentSong.title}' by ${currentSong.artist}")
                
                if (currentSong.previewUrl.isNotEmpty()) {
                    // Song has preview URL - play it
                    try {
                        audioService.playAudio(currentSong.previewUrl)
                        delay(30000) // Wait 30 seconds for audio playback
                        audioService.stopAudio()
                    } catch (e: Exception) {
                        println("GameViewModel: Error playing audio: ${e.message}")
                        delay(15000) // Wait 15 seconds if audio fails
                    }
                } else {
                    // Song has no preview URL - just wait and show info
                    println("GameViewModel: Song '${currentSong.title}' has no preview URL")
                    delay(15000) // Wait 15 seconds for songs without audio
                }
                
                println("GameViewModel: Playback finished for '${currentSong.title}'")
            }
            _gameState.value = _gameState.value.copy(isPlaying = false)
        }
    }

    fun showAnswer() {
        _gameState.value = _gameState.value.copy(showAnswer = true)

        viewModelScope.launch {
            delay(10000)
            hideAnswer()
        }
    }

    private fun hideAnswer() {
        val currentState = _gameState.value
        val updatedCategories = currentState.availableCategories.filter { category ->
            currentState.songsByCategory[category]?.isNotEmpty() == true
        }

        println("GameViewModel: hideAnswer - availableCategories before filtering: ${currentState.availableCategories.size}")
        println("GameViewModel: hideAnswer - updatedCategories after filtering: ${updatedCategories.size}")

        _gameState.value = currentState.copy(
            selectedCategory = null,
            currentSong = null,
            isPlaying = false,
            showAnswer = false,
            availableCategories = updatedCategories
        )

        if (updatedCategories.isEmpty()) {
            println("GameViewModel: hideAnswer - No categories with songs left, ending game")
            // Очищаем ресурсы AudioService при завершении игры
            audioService.cleanup()
            _currentScreen.value = GameScreen.GameFinished
        } else {
            println("GameViewModel: hideAnswer - Returning to category selection with ${updatedCategories.size} categories")
            _currentScreen.value = GameScreen.CategorySelection
        }
    }

    fun restartGame() {
        // Очищаем ресурсы AudioService
        audioService.cleanup()
        
        _gameState.value = GameState()
        _currentScreen.value = GameScreen.PlayerSelection
    }
    
    fun hideSnackbar() {
        println("GameViewModel: Hiding snackbar")
        _gameState.value = _gameState.value.copy(snackbarMessage = null)
        println("GameViewModel: Snackbar message cleared")
    }
    
    fun testSystemSound() {
        println("GameViewModel: Testing system sound")
        audioService.testSystemSound()
    }
} 