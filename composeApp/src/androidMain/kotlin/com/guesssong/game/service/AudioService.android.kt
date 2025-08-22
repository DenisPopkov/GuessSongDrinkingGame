package com.guesssong.game.service

import android.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.io.IOException

actual class AudioService {
    private var mediaPlayer: MediaPlayer? = null
    private val maxAudioSize = 5 * 1024 * 1024 // 5MB limit

    actual suspend fun playAudio(audioPath: String) {
        withContext(Dispatchers.IO) {
            try {
                // Stop any existing playback
                stopAudio()
                
                // Check if URL is valid
                if (!audioPath.startsWith("http")) {
                    println("Android: Invalid audio URL: $audioPath")
                    return@withContext
                }
                
                // Check file size before downloading
                val url = URL(audioPath)
                val connection = url.openConnection()
                val contentLength = connection.contentLength
                
                if (contentLength > maxAudioSize) {
                    println("Android: Audio file too large: $contentLength bytes, limit: $maxAudioSize")
                    return@withContext
                }
                
                println("Android: Audio file size: $contentLength bytes")
                
                // Create new MediaPlayer
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(audioPath)
                    prepare()
                    start()
                }
                
                println("Android: Playing audio from URL: $audioPath")
                
                // Set up error listener
                mediaPlayer?.setOnErrorListener { mp, what, extra ->
                    println("Android: MediaPlayer error: what=$what, extra=$extra")
                    stopAudio()
                    true
                }
                
                // Set up completion listener
                mediaPlayer?.setOnCompletionListener {
                    println("Android: Audio playback completed")
                    stopAudio()
                }

            } catch (e: IOException) {
                e.printStackTrace()
                println("Android: Network error playing audio: ${e.message}")
                stopAudio()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Android: Error playing audio: ${e.message}")
                stopAudio()
            }
        }
    }

    actual fun stopAudio() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
            mediaPlayer = null
            println("Android: Audio stopped and resources cleared")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Android: Error stopping audio: ${e.message}")
        }
    }

    actual fun testSystemSound() {
        try {
            println("Android: Testing system sound")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Android: Error playing system sound: ${e.message}")
        }
    }
    
    actual fun cleanup() {
        stopAudio()
        println("Android: AudioService cleanup completed")
    }
}
