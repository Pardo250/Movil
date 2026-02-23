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
        R.drawable.cartagena,
        R.drawable.valle_del_cocora,
        R.drawable.santamarta,
        R.drawable.medellin,
        R.drawable.atardecer,
        R.drawable.catedral
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
    val cs = MaterialTheme.colorScheme

    Scaffold(
        modifier = modifier,
        containerColor = cs.background,
        bottomBar = {
            BottomNavBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(cs.background)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            SearchBar()

            Spacer(modifier = Modifier.height(16.dp))

            MapCard()

            Spacer(modifier = Modifier.height(16.dp))

            CategoryChips(
                selectedIndex = state.selectedCategoryIndex,
                onSelected = onCategorySelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.recommended_for_you),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = cs.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Para que no se “coma” el bottomBar
            RecommendationGrid(
                places = state.places,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(cs.surfaceVariant),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.cd_search),
                tint = cs.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search_placeholder),
                color = cs.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun MapCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
    val cs = MaterialTheme.colorScheme

    val containerColor = if (selected) cs.primary else cs.secondaryContainer
    val contentColor = if (selected) cs.onPrimary else cs.onSecondaryContainer

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
fun RecommendationGrid(
    places: List<Int>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(places) { image ->
            Image(
                painter = painterResource(id = image),
                contentDescription = stringResource(R.string.cd_recommendation_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme

    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = modifier.height(70.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surface)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.nav_explore),
                tint = cs.onSurface
            )
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(R.string.nav_home),
                tint = cs.onSurface
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.nav_profile),
                tint = cs.onSurface
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedScreenLightPreview() {
    CondorappTheme(darkTheme = false) { FeedScreenRoute() }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedScreenDarkPreview() {
    CondorappTheme(darkTheme = true) { FeedScreenRoute() }
}