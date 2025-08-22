package com.guesssong.game.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesssong.game.strings.Strings
import com.guesssong.game.theme.SfFontFamily
import com.guesssong.game.theme.SpacingSystem

@Composable
fun GameFinishedScreen(
    onRestartGame: () -> Unit
) {
    val fonts = SfFontFamily()
    val spacing = SpacingSystem()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(size = 120.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(color = 0xFFFFD700)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Игра окончена",
                    modifier = Modifier.size(size = 64.dp),
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(height = spacing.l))

            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                text = Strings.GAME_OVER_TITLE,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fonts,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            Text(
                modifier = Modifier
                    .padding(bottom = spacing.m),
                text = Strings.ALL_SONGS_PLAYED,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fonts,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.l)
                    .height(height = spacing.xxl),
                onClick = onRestartGame,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(color = 0xFF4CAF50)
                ),
            ) {
                Text(
                    text = Strings.RESTART_GAME_BUTTON,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fonts,
                    color = Color.White,
                )
            }
        }
    }
} 