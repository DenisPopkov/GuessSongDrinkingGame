package com.guesssong.game.data

import com.guesssong.game.domain.Song
import kotlinx.coroutines.delay

class DeezerSongDownloadService {
    private val deezerApiService = DeezerApiService()

    suspend fun downloadSongs(category: String): List<Song> {
        val targetCount = 3 // Уменьшаем с 5 до 3 для максимальной экономии памяти
        val maxAttempts = 1 // Оставляем 1 попытку
        
        println("DeezerSongDownloadService: Starting search for category: $category")

        // Только одна стратегия для экономии памяти
        val tracksFromStrategy1 = if (category == "2020-е") {
            searchByYears(targetCount, maxAttempts)
        } else {
            searchByGenres(category, targetCount, maxAttempts)
        }

        // Если получили достаточно треков, возвращаем результат
        if (tracksFromStrategy1.count { it.preview.isNotEmpty() } >= targetCount) {
            return processAndReturnTracks(tracksFromStrategy1, targetCount, category)
        }

        // Небольшая пауза для освобождения памяти
        delay(100)

        // Только одна дополнительная стратегия
        val tracksFromStrategy2 = searchTopCharts(targetCount)
        val combinedTracks = (tracksFromStrategy1 + tracksFromStrategy2).distinctBy { it.id }
        
        return processAndReturnTracks(combinedTracks, targetCount, category)
    }

    private suspend fun searchByYears(targetCount: Int, maxAttempts: Int): List<DeezerTrack> {
        val years = listOf(2020, 2021, 2022, 2023, 2024)
        val allTracks = mutableListOf<DeezerTrack>()

        for (year in years.take(maxAttempts)) {
            try {
                val yearTracks = deezerApiService.searchTracksByYear(year, targetCount)
                allTracks.addAll(yearTracks)
                println("DeezerSongDownloadService: Found ${yearTracks.size} tracks for year $year")

                if (allTracks.count { it.preview.isNotEmpty() } >= targetCount) break
            } catch (e: Exception) {
                println("DeezerSongDownloadService: Year search failed for $year: ${e.message}")
            }
        }
        
        return allTracks
    }

    private suspend fun searchByGenres(category: String, targetCount: Int, maxAttempts: Int): List<DeezerTrack> {
        val genres = getGenresForCategory(category)
        val allTracks = mutableListOf<DeezerTrack>()

        for (genre in genres.take(maxAttempts)) {
            try {
                val genreTracks = deezerApiService.searchTracksByGenre(genre, targetCount)
                allTracks.addAll(genreTracks)
                println("DeezerSongDownloadService: Found ${genreTracks.size} tracks for genre $genre")

                if (allTracks.count { it.preview.isNotEmpty() } >= targetCount) break
            } catch (e: Exception) {
                println("DeezerSongDownloadService: Genre search failed for $genre: ${e.message}")
            }
        }
        
        return allTracks
    }

    private suspend fun searchPopularTracks(category: String, targetCount: Int, maxAttempts: Int): List<DeezerTrack> {
        val searchQueries = getSearchQueriesForCategory(category)
        val allTracks = mutableListOf<DeezerTrack>()

        for (query in searchQueries.take(maxAttempts)) {
            try {
                val popularTracks = deezerApiService.searchPopularTracks(query, targetCount)
                allTracks.addAll(popularTracks)
                println("DeezerSongDownloadService: Found ${popularTracks.size} popular tracks for $query")

                if (allTracks.count { it.preview.isNotEmpty() } >= targetCount) break
            } catch (e: Exception) {
                println("DeezerSongDownloadService: Popular tracks search failed for $query: ${e.message}")
            }
        }
        
        return allTracks
    }

    private suspend fun searchTopCharts(targetCount: Int): List<DeezerTrack> {
        return try {
            val topChartsTracks = deezerApiService.searchTopCharts(targetCount)
            println("DeezerSongDownloadService: Found ${topChartsTracks.size} top charts tracks")
            topChartsTracks
        } catch (e: Exception) {
            println("DeezerSongDownloadService: Top charts search failed: ${e.message}")
            emptyList()
        }
    }

    private suspend fun searchBasicTracks(category: String, targetCount: Int, maxAttempts: Int): List<DeezerTrack> {
        val searchQueries = getSearchQueriesForCategory(category)
        val allTracks = mutableListOf<DeezerTrack>()

        for (query in searchQueries.take(maxAttempts)) {
            try {
                val tracks = deezerApiService.searchTracks(query, targetCount)
                allTracks.addAll(tracks)
                println("DeezerSongDownloadService: Found ${tracks.size} tracks for $query")

                if (allTracks.count { it.preview.isNotEmpty() } >= targetCount) break
            } catch (e: Exception) {
                println("DeezerSongDownloadService: Basic search failed for $query: ${e.message}")
            }
        }
        
        return allTracks
    }

    private fun processAndReturnTracks(tracks: List<DeezerTrack>, targetCount: Int, category: String): List<Song> {
        val finalTracks = tracks
            .sortedWith(compareByDescending<DeezerTrack> { it.preview.isNotEmpty() }
                .thenByDescending { it.rank })
            .take(targetCount)

        val tracksWithPreviewFinal = finalTracks.count { it.preview.isNotEmpty() }
        println("DeezerSongDownloadService: Final result - ${finalTracks.size} tracks for $category ($tracksWithPreviewFinal with preview URLs)")

        return finalTracks.map { track ->
            Song(
                id = track.id.toString(),
                title = track.title,
                artist = track.artist.name,
                previewUrl = track.preview,
                category = category
            )
        }
    }

    private fun getGenresForCategory(category: String): List<String> {
        return when (category) {
            "Рэп" -> listOf("rap", "hip-hop", "trap", "gangster rap")
            "2020-е" -> listOf("pop", "hip-hop", "r-n-b", "trap")
            "Зарубежное" -> listOf("pop", "rock", "dance", "indie")
            "Русское" -> listOf("russian", "pop", "rock", "indie")
            else -> listOf("pop", "rock", "dance", "indie")
        }
    }

    private fun getSearchQueriesForCategory(category: String): List<String> {
        return when (category) {
            "Рэп" -> listOf(
                "Drake God's Plan",
                "Kendrick Lamar HUMBLE",
                "Travis Scott SICKO MODE",
                "Cardi B Bodak Yellow",
                "Eminem Lose Yourself",
                "Kanye West Stronger",
                "Jay-Z Empire State of Mind",
                "Nicki Minaj Super Bass",
                "Lil Nas X Old Town Road",
                "Post Malone Rockstar",
                "J. Cole No Role Modelz",
                "21 Savage Bank Account",
                "Migos Bad and Boujee",
                "Future Mask Off",
                "Lil Baby Drip Too Hard"
            )

            "2020-е" -> listOf(
                "Olivia Rodrigo drivers license",
                "Lil Nas X MONTERO",
                "The Weeknd Blinding Lights",
                "Dua Lipa Levitating",
                "Harry Styles As It Was",
                "Billie Eilish bad guy",
                "Lizzo About Damn Time",
                "Drake Toosie Slide",
                "Doja Cat Say So",
                "Justin Bieber Stay",
                "Taylor Swift Anti-Hero",
                "Bad Bunny Me Porto Bonito",
                "Glass Animals Heat Waves",
                "The Kid LAROI Stay",
                "Megan Thee Stallion Savage"
            )

            "Зарубежное" -> listOf(
                "Shakira Hips Don't Lie",
                "Rihanna Umbrella",
                "Ed Sheeran Shape of You",
                "Adele Rolling in the Deep",
                "Bruno Mars Uptown Funk",
                "Maroon 5 Sugar",
                "Post Malone Circles",
                "The Weeknd Starboy",
                "Sam Smith Stay With Me",
                "Billie Eilish Ocean Eyes",
                "Coldplay Viva La Vida",
                "Imagine Dragons Radioactive",
                "OneRepublic Counting Stars",
                "The Chainsmokers Closer",
                "Calvin Harris This Is What You Came For"
            )

            "Русское" -> listOf(
                "Мумий Тролль Владивосток 2000",
                "Тимати Где же ты",
                "Земфира Ариведерчи",
                "Ленинград В Питере пить",
                "Юта Про любовь",
                "Баста Сансара",
                "Григорий Лепс Рюмка водки на столе",
                "Макс Корж Малиновый закат",
                "Кино Спокойная ночь",
                "ДДТ Что такое осень",
                "Би-2 Полковнику никто не пишет",
                "Агата Кристи Как на войне",
                "Сплин Орбит без сахара",
                "Чайф Аргентина-Ямайка",
                "Наутилус Помпилиус Крылья"
            )

            else -> listOf(category)
        }
    }
} 