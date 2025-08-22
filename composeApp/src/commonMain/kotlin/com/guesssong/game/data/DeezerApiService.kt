package com.guesssong.game.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class DeezerApiService {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        // Отключаем логирование полностью для экономии памяти
    }

    private val baseUrl = "https://api.deezer.com"

    suspend fun searchTracks(query: String, limit: Int = 10): List<DeezerTrack> {
        try {
            println("Searching Deezer for: $query")

            val response = withTimeout(8000) { // Уменьшаем таймаут еще больше
                httpClient.get("$baseUrl/search") {
                    parameter("q", query)
                    parameter("limit", minOf(limit, 10)) // Еще больше ограничиваем размер
                }
            }

            if (response.status.isSuccess()) {
                val searchResponse: DeezerSearchResponse = response.body()
                val tracks = searchResponse.data

                // Более агрессивная фильтрация
                val filteredTracks = tracks
                    .filter { it.preview.isNotEmpty() }
                    .filter { it.rank > 200000 } // Увеличиваем минимальный ранг еще больше
                    .sortedByDescending { it.rank }
                    .take(limit)

                println("Found ${filteredTracks.size} filtered tracks on Deezer")
                return filteredTracks
            } else {
                println("Deezer API returned error status: ${response.status}")
                return emptyList()
            }
        } catch (e: TimeoutCancellationException) {
            println("Timeout searching tracks on Deezer after 8 seconds")
            return emptyList()
        } catch (e: Exception) {
            println("Deezer API error: ${e.message}")
            return emptyList()
        }
    }

    suspend fun searchTracksByGenre(genre: String, limit: Int = 10): List<DeezerTrack> {
        try {
            println("Searching Deezer for genre: $genre")

            val allTracks = mutableListOf<DeezerTrack>()

            // Только один запрос для экономии памяти
            val response = withTimeout(8000) {
                httpClient.get("$baseUrl/search") {
                    parameter("q", "genre:$genre")
                    parameter("limit", minOf(limit * 2, 10)) // Ограничиваем размер еще больше
                }
            }

            if (response.status.isSuccess()) {
                val searchResponse: DeezerSearchResponse = response.body()
                allTracks.addAll(searchResponse.data)
            }

            val tracksWithPreview = allTracks
                .distinctBy { it.id }
                .filter { it.preview.isNotEmpty() }
                .filter { it.rank > 200000 } // Увеличиваем минимальный ранг еще больше
                .sortedByDescending { it.rank }
                .take(limit)

            println("Found ${tracksWithPreview.size} tracks with preview for genre $genre on Deezer")
            return tracksWithPreview

        } catch (e: Exception) {
            println("Error searching tracks by genre on Deezer: ${e.message}")
            return emptyList()
        }
    }

    suspend fun searchPopularTracks(query: String, limit: Int = 10): List<DeezerTrack> {
        try {
            println("Searching popular tracks on Deezer for: $query")

            val response = withTimeout(8000) {
                httpClient.get("$baseUrl/search") {
                    parameter("q", query)
                    parameter("limit", minOf(limit * 2, 10)) // Ограничиваем размер еще больше
                    parameter("order", "RATING_DESC")
                }
            }

            if (response.status.isSuccess()) {
                val searchResponse: DeezerSearchResponse = response.body()
                val tracks = searchResponse.data

                val popularTracksWithPreview = tracks
                    .filter { it.rank > 200000 } // Увеличиваем минимальный ранг еще больше
                    .filter { it.preview.isNotEmpty() }
                    .sortedByDescending { it.rank }
                    .take(limit)

                println("Found ${popularTracksWithPreview.size} popular tracks with preview for $query on Deezer")
                return popularTracksWithPreview
            }

            return emptyList()
        } catch (e: Exception) {
            println("Error searching popular tracks on Deezer: ${e.message}")
            return emptyList()
        }
    }

    suspend fun searchTracksByYear(year: Int, limit: Int = 10): List<DeezerTrack> {
        try {
            println("Searching Deezer for year: $year")

            val response = withTimeout(8000) {
                httpClient.get("$baseUrl/search") {
                    parameter("q", "year:$year")
                    parameter("limit", minOf(limit * 2, 10)) // Ограничиваем размер еще больше
                    parameter("order", "RATING_DESC")
                }
            }

            if (response.status.isSuccess()) {
                val searchResponse: DeezerSearchResponse = response.body()
                val tracks = searchResponse.data

                val tracksWithPreview = tracks
                    .filter { it.preview.isNotEmpty() }
                    .filter { it.rank > 200000 } // Увеличиваем минимальный ранг еще больше
                    .sortedByDescending { it.rank }
                    .take(limit)

                println("Found ${tracksWithPreview.size} tracks with preview for year $year on Deezer")
                return tracksWithPreview
            }

            return emptyList()
        } catch (e: Exception) {
            println("Error searching tracks by year on Deezer: ${e.message}")
            return emptyList()
        }
    }

    suspend fun searchTopCharts(limit: Int = 10): List<DeezerTrack> {
        try {
            println("Searching top charts on Deezer...")

            val response = withTimeout(8000) {
                httpClient.get("$baseUrl/chart/0/tracks") {
                    parameter("limit", minOf(limit * 2, 10)) // Ограничиваем размер еще больше
                }
            }

            if (response.status.isSuccess()) {
                val searchResponse: DeezerSearchResponse = response.body()
                val tracks = searchResponse.data

                val tracksWithPreview = tracks
                    .filter { it.preview.isNotEmpty() }
                    .filter { it.rank > 200000 } // Увеличиваем минимальный ранг еще больше
                    .sortedByDescending { it.rank }
                    .take(limit)

                println("Found ${tracksWithPreview.size} top charts tracks with preview on Deezer")
                return tracksWithPreview
            }

            return emptyList()
        } catch (e: Exception) {
            println("Error searching top charts on Deezer: ${e.message}")
            return emptyList()
        }
    }
}


