@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.condorapp.R
import com.example.condorapp.data.FeedPlace
import com.example.condorapp.data.local.FeedRepository
import com.example.condorapp.ui.theme.CondorappTheme

data class FeedUiState(
    val selectedCategoryIndex: Int = 0,
    val places: List<FeedPlace> = emptyList()
)

@Composable
fun FeedScreenRoute(
    navController: NavHostController, // Agregamos el controller para la barra
    modifier: Modifier = Modifier
) {
    val initialPlaces = remember { FeedRepository.getPlaces() }
    var state by remember { mutableStateOf(FeedUiState(places = initialPlaces)) }

    FeedScreenContent(
        state = state,
        navController = navController,
        modifier = modifier,
        onCategorySelected = { index -> state = state.copy(selectedCategoryIndex = index) }
    )
}

@Composable
fun FeedScreenContent(
    state: FeedUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onCategorySelected: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
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
                color = DesignGreenDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationGrid(
                places = state.places,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // BARRA INFERIOR INTEGRADA CON TU LÓGICA DE NAVEGACIÓN
        BottomFloatingBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun FeedSearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.cd_search),
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.search_placeholder),
                color = Color.LightGray,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun MapCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.mapa),
            contentDescription = stringResource(R.string.cd_map_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CategoryChips(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    val categories = listOf(
        R.string.category_landscape,
        R.string.category_beaches,
        R.string.category_cultural,
        R.string.category_hotels
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
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
fun FilterChip(
    modifier: Modifier = Modifier,
    textRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (selected) DesignGreenDark else DesignGreenLight,
        contentColor = if (selected) Color.White else DesignGreenDark
    ) {
        Text(
            text = stringResource(textRes),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun RecommendationGrid(
    places: List<FeedPlace>,
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
            Image(
                painter = painterResource(id = place.imageRes),
                contentDescription = stringResource(R.string.cd_recommendation_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(0.8f)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}