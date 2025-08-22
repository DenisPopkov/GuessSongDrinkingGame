package com.guesssong.game.service

import kotlinx.coroutines.delay

actual class AudioService {
    actual suspend fun playAudio(audioPath: String) {
        println("iOS: AudioService.playAudio called with: $audioPath")
        println("iOS: Would play audio from URL: $audioPath")
        
        // Временная симуляция аудио для тестирования игры
        // В реальности здесь будет вызов нативных функций
        println("iOS: Simulating audio playback for 30 seconds...")
        delay(30000) // 30 секунд симуляции
        println("iOS: Audio simulation finished")
    }
    
    actual fun stopAudio() {
        println("iOS: AudioService.stopAudio called")
        println("iOS: Would stop audio playback")
    }
    
    actual fun testSystemSound() {
        println("iOS: AudioService.testSystemSound called")
        println("iOS: Would play system sound")
    }
    
    actual fun cleanup() {
        println("iOS: AudioService.cleanup called")
        println("iOS: Would cleanup audio resources")
    }
} 
