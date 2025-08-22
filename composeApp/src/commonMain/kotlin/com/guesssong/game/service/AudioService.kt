package com.guesssong.game.service

expect class AudioService() {
    suspend fun playAudio(audioPath: String)
    fun stopAudio()
    fun testSystemSound()
    fun cleanup()
}
