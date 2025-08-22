package com.guesssong.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.guesssong.game.theme.SfFontFamily
import com.guesssong.game.theme.SpacingSystem

@Composable
fun LoadingScreen(
    loadingText: String = "Загрузка песен..."
) {
    val fonts = SfFontFamily()
    val spacing = SpacingSystem()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.padding(bottom = spacing.l)
            )

            Text(
                text = loadingText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = fonts,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = spacing.l)
            )
        }
    }
} 