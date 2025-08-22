package com.guesssong.game.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import guesssongdrinkinggame.composeapp.generated.resources.MavenPro_Bold
import guesssongdrinkinggame.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun SfFontFamily() = FontFamily(
    Font(Res.font.MavenPro_Bold, weight = FontWeight.Bold),
)
