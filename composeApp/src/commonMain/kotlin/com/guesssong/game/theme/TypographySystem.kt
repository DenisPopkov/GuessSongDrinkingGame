package com.guesssong.game.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import guesssongdrinkinggame.composeapp.generated.resources.Res
import guesssongdrinkinggame.composeapp.generated.resources.Sf_Pro_Medium
import org.jetbrains.compose.resources.Font

@Composable
fun SfFontFamily() = FontFamily(
    Font(Res.font.Sf_Pro_Medium, weight = FontWeight.Bold),
)
