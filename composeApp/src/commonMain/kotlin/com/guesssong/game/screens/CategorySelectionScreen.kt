package com.guesssong.game.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guesssong.game.domain.Category
import com.guesssong.game.theme.SfFontFamily
import com.guesssong.game.theme.SpacingSystem
import org.jetbrains.compose.resources.painterResource

@Composable
fun CategorySelectionScreen(
    availableCategories: List<Category>,
    onCategorySelected: (Category) -> Unit
) {
    val spacing = SpacingSystem()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(1),
            contentPadding = PaddingValues(all = spacing.m),
            horizontalArrangement = Arrangement.spacedBy(space = spacing.m),
            verticalArrangement = Arrangement.spacedBy(space = spacing.m),
        ) {
            items(availableCategories) { category ->
                CategoryCard(
                    category = category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
    }
}


@Composable
private fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    val spacing = SpacingSystem()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 160.dp)
            .clip(shape = RoundedCornerShape(size = spacing.xxs))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(category.imageResource),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}
