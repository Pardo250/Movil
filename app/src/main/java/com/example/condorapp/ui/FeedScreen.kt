package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

data class FeedUiState(
    val selectedCategoryIndex: Int = 0,
    val places: List<Int> = listOf(
        R.drawable.cartagena, R.drawable.valle_del_cocora, R.drawable.santamarta,
        R.drawable.medellin, R.drawable.atardecer, R.drawable.catedral
    )
)

@Composable
fun FeedScreenRoute(
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(FeedUiState()) }

    FeedScreenContent(
        state = state,
        modifier = modifier,
        onCategorySelected = { index -> state = state.copy(selectedCategoryIndex = index) }
    )
}

@Composable
fun FeedScreenContent(
    state: FeedUiState,
    modifier: Modifier = Modifier,
    onCategorySelected: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(16.dp))
        MapCard()
        Spacer(modifier = Modifier.height(16.dp))
        CategoryChips(selectedIndex = state.selectedCategoryIndex, onSelected = onCategorySelected)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.recommended_for_you),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecommendationGrid(places = state.places)
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.search_placeholder), color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
        }
    }
}

@Composable
fun MapCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth().height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CategoryChips(modifier: Modifier = Modifier, selectedIndex: Int, onSelected: (Int) -> Unit) {
    val categories = listOf(R.string.category_landscape, R.string.category_beaches, R.string.category_cultural, R.string.category_hotels)
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.forEachIndexed { index, res ->
            FilterChip(
                textRes = res,
                selected = selectedIndex == index,
                onClick = { onSelected(index) }
            )
        }
    }
}

@Composable
fun FilterChip(modifier: Modifier = Modifier, textRes: Int, selected: Boolean, onClick: () -> Unit) {
    val containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer

    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = stringResource(textRes),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun RecommendationGrid(places: List<Int>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(places) { image ->
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    CondorappTheme { FeedScreenRoute() }
}
