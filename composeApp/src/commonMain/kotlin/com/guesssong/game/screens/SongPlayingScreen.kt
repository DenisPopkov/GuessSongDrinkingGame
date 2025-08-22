package com.guesssong.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.guesssong.game.strings.Strings
import com.guesssong.game.theme.SfFontFamily
import com.guesssong.game.theme.SpacingSystem
import com.guesssong.game.domain.Song

@Composable
fun SongPlayingScreen(
    song: Song,
    isPlaying: Boolean,
    showAnswer: Boolean,
    onShowAnswer: () -> Unit,
    onTestSystemSound: () -> Unit = {}
) {
    val fonts = SfFontFamily()
    val spacing = SpacingSystem()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isPlaying) {
                if (song.previewUrl.isEmpty()) {
                    // Show info for songs without preview
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.s)
                    ) {
                        Text(
                            text = "🎵",
                            fontSize = 48.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = song.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = fonts,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = song.artist,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fonts,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Попробуйте угадать по названию и исполнителю",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fonts,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "15 секунд до ответа",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fonts,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Show normal playback for songs with preview
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.xxs)
                    ) {
                        Text(
                            text = "Проигрывание...",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fonts,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "30 секунд до ответа",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fonts,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (showAnswer) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(spacing.xs)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = spacing.xxs),
                        text = song.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fonts,
                        color = Color(0xFF2196F3),
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = song.artist,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = fonts,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    
                    if (song.previewUrl.isEmpty()) {
                        Text(
                            text = "🎵 Песня без аудио",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fonts,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "🔊 Песня с аудио",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = fonts,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.s)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.l)
                            .height(height = spacing.xxl),
                        onClick = onShowAnswer,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF2196F3)
                        ),
                    ) {
                        Text(
                            text = Strings.SHOW_ANSWER_BUTTON,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = fonts,
                            color = Color.White
                        )
                    }
                    
                    // Debug button for testing system sound (only in debug builds)
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.l)
                            .height(height = spacing.l),
                        onClick = onTestSystemSound,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF666666)
                        ),
                    ) {
                        Text(
                            text = "🔊 Test Sound",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = fonts,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
} 