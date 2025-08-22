package com.guesssong.game.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesssong.game.strings.Strings
import com.guesssong.game.theme.SfFontFamily
import com.guesssong.game.theme.SpacingSystem
import guesssongdrinkinggame.composeapp.generated.resources.Res
import guesssongdrinkinggame.composeapp.generated.resources.ic_add
import guesssongdrinkinggame.composeapp.generated.resources.ic_remove
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayerSelectionScreen(
    playerCount: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onStartGame: () -> Unit
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
            Text(
                modifier = Modifier
                    .padding(bottom = spacing.l),
                text = Strings.PLAYER_COUNT_TITLE,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fonts,
                color = Color.White,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.m)
            ) {
                Button(
                    modifier = Modifier
                        .size(size = 51.dp),
                    onClick = onDecrement,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(color = 0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(size = spacing.xxs)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(size = spacing.m),
                        painter = painterResource(Res.drawable.ic_remove),
                        contentDescription = "Уменьшить",
                        tint = Color.White,
                    )
                }

                Text(
                    modifier = Modifier
                        .width(width = 80.dp),
                    text = playerCount.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fonts,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Button(
                    modifier = Modifier
                        .size(size = 51.dp),
                    onClick = onIncrement,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(color = 0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(spacing.xxs)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(size = spacing.m),
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = "Увеличить",
                        tint = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.height(height = spacing.l))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.l)
                    .height(height = 58.dp),
                onClick = onStartGame,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF2196F3)
                ),
            ) {
                Text(
                    text = Strings.START_BUTTON,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = fonts,
                    color = Color.White
                )
            }
        }
    }
} 