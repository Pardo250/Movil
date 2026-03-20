@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.feed

import androidx.compose.foundation.background
import coil.compose.AsyncImage
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.condorapp.R
import com.example.condorapp.data.FeedPlace
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de exploración (Feed). Conecta el FeedViewModel con el
 * contenido stateless.
 */
@Composable
fun FeedScreenRoute(
        modifier: Modifier = Modifier,
        viewModel: FeedViewModel = viewModel(),
        onPlaceClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FeedScreenContent(
            state = uiState,
            modifier = modifier,
            onCategorySelected = viewModel::onCategorySelected,
            onPlaceClick = { place -> onPlaceClick(place.location) }
    )
}

/** Contenido stateless de la pantalla de exploración. */
@Composable
fun FeedScreenContent(
        state: FeedUiState,
        modifier: Modifier = Modifier,
        onCategorySelected: (Int) -> Unit,
        onPlaceClick: (FeedPlace) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(40.dp))
            FeedSearchBar()
            Spacer(modifier = Modifier.height(20.dp))
            MapCard()
            Spacer(modifier = Modifier.height(20.dp))
            CategoryChips(
                    selectedIndex = state.selectedCategoryIndex,
                    onSelected = onCategorySelected
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                    text = stringResource(R.string.recommended_for_you),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecommendationGrid(
                    places = state.places,
                    onPlaceClick = onPlaceClick,
                    modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/** Barra de búsqueda decorativa. */
@Composable
fun FeedSearchBar(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
            modifier = modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(28.dp),
            color = colorScheme.surface,
            shadowElevation = 4.dp
    ) {
        Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.cd_search),
                    tint = colorScheme.outline,
                    modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                    text = stringResource(R.string.search_placeholder),
                    color = colorScheme.outline.copy(alpha = 0.6f),
                    fontSize = 16.sp
            )
        }
    }
}

/** Tarjeta con imagen del mapa. */
@Composable
fun MapCard(modifier: Modifier = Modifier) {
    Card(
            shape = RoundedCornerShape(24.dp),
            modifier = modifier.fillMaxWidth().height(180.dp),
            elevation = CardDefaults.cardElevation(8.dp)
    ) {
        androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.mapa),
                contentDescription = stringResource(R.string.cd_map_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
        )
    }
}

/** Chips de categorías seleccionables. */
@Composable
fun CategoryChips(modifier: Modifier = Modifier, selectedIndex: Int, onSelected: (Int) -> Unit) {
    val categories =
            listOf(
                    R.string.category_landscape,
                    R.string.category_beaches,
                    R.string.category_cultural,
                    R.string.category_hotels
            )
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        categories.forEachIndexed { index, res ->
            FilterChipItem(
                    textRes = res,
                    selected = selectedIndex == index,
                    onClick = { onSelected(index) }
            )
        }
    }
}

/** Chip individual de filtro de categoría. */
@Composable
fun FilterChipItem(
        modifier: Modifier = Modifier,
        textRes: Int,
        selected: Boolean,
        onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
            modifier = modifier.clickable { onClick() },
            shape = RoundedCornerShape(20.dp),
            color = if (selected) colorScheme.primary else colorScheme.surfaceVariant,
            contentColor = if (selected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
    ) {
        Text(
                text = stringResource(textRes),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
        )
    }
}

/** Grid de recomendaciones con imágenes clicables. */
@Composable
fun RecommendationGrid(
        places: List<FeedPlace>,
        onPlaceClick: (FeedPlace) -> Unit,
        modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(places) { place ->
            AsyncImage(
                    model = place.imageUrl,
                    contentDescription = stringResource(R.string.cd_recommendation_image),
                    contentScale = ContentScale.Crop,
                    modifier =
                            Modifier.aspectRatio(0.8f).clip(RoundedCornerShape(20.dp)).clickable {
                                onPlaceClick(place)
                            }
            )
        }
    }
}

@Preview(showBackground = true, name = "Feed - Light")
@Composable
fun FeedScreenLightPreview() {
    CondorappTheme(darkTheme = false) { FeedScreenRoute(onPlaceClick = {}) }
}

@Preview(showBackground = true, name = "Feed - Dark")
@Composable
fun FeedScreenDarkPreview() {
    CondorappTheme(darkTheme = true) { FeedScreenRoute(onPlaceClick = {}) }
}
