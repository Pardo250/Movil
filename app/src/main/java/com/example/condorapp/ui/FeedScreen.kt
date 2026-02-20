package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.condorapp.R

/* ---------------- UI State (Entidad UI) ---------------- */

data class FeedUiState(
    val selectedCategoryIndex: Int = 0,
    val places: List<Int> = emptyList()
)

/* ---------------- Route (State Hosting) ---------------- */

@Composable
fun FeedScreenRoute(
    modifier: Modifier = Modifier
) {
    val initialPlaces = remember {
        listOf(
            R.drawable.cartagena,
            R.drawable.vallecocora,
            R.drawable.santamarta,
            R.drawable.medellin,
            R.drawable.atardecer,
            R.drawable.catedral,
            R.drawable.cartagena,
            R.drawable.vallecocora,
            R.drawable.santamarta,
            R.drawable.medellin,
            R.drawable.atardecer,
            R.drawable.catedral
        )
    }

    var state by remember {
        mutableStateOf(
            FeedUiState(
                selectedCategoryIndex = 0,
                places = initialPlaces
            )
        )
    }

    FeedScreenContent(
        state = state,
        modifier = modifier,
        onCategorySelected = { index ->
            state = state.copy(selectedCategoryIndex = index)
        }
    )
}

/* ---------------- Content (Stateless UI) ---------------- */

@Composable
fun FeedScreenContent(
    state: FeedUiState,
    modifier: Modifier = Modifier,
    onCategorySelected: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.Olivafeed))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

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
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationGrid(places = state.places)

            Spacer(modifier = Modifier.weight(1f))

            BottomNavBar()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedScreenPreview() {
    FeedScreenRoute()
}

/* ---------------- Components ---------------- */

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFD9D2C7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.cd_search)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search_placeholder),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MapCard(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
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
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            textRes = R.string.category_landscape,
            selected = selectedIndex == 0,
            onClick = { onSelected(0) }
        )
        FilterChip(
            textRes = R.string.category_beaches,
            selected = selectedIndex == 1,
            onClick = { onSelected(1) }
        )
        FilterChip(
            textRes = R.string.category_cultural,
            selected = selectedIndex == 2,
            onClick = { onSelected(2) }
        )
        FilterChip(
            textRes = R.string.category_hotels,
            selected = selectedIndex == 3,
            onClick = { onSelected(3) }
        )
    }
}

@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    textRes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = if (selected) Color(0xFF3E5F2C) else Color(0xFF9FB18A)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .clickable { onClick() } // ✅ interacción
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = stringResource(textRes), color = Color.White)
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
        modifier = modifier.height(320.dp)
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
fun BottomNavBar(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = stringResource(R.string.nav_explore)
            )
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = stringResource(R.string.nav_home)
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.nav_profile)
            )
        }
    }
}
