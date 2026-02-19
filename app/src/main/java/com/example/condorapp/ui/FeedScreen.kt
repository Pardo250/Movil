package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier
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

            CategoryChips()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.recommended_for_you),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            RecommendationGrid()

            Spacer(modifier = Modifier.weight(1f))

            BottomNavBar()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen()
}

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

@Preview(showBackground = false)
@Composable
fun SearchBarPreview() {
    SearchBar()
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

@Preview(showBackground = false)
@Composable
fun MapCardPreview() {
    MapCard()
}

@Composable
fun CategoryChips(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(textRes = R.string.category_landscape, selected = true)
        FilterChip(textRes = R.string.category_beaches, selected = false)
        FilterChip(textRes = R.string.category_cultural, selected = false)
        FilterChip(textRes = R.string.category_hotels, selected = false)
    }
}

@Preview(showBackground = false)
@Composable
fun CategoryChipsPreview() {
    CategoryChips()
}

@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    textRes: Int,
    selected: Boolean
) {
    val background = if (selected) Color(0xFF3E5F2C) else Color(0xFF9FB18A)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = stringResource(textRes), color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun FilterChipPreview() {
    FilterChip(textRes = R.string.category_landscape, selected = true)
}

@Composable
fun RecommendationGrid(
    modifier: Modifier = Modifier
) {
    val places = listOf(
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

@Preview(showBackground = false)
@Composable
fun RecommendationGridPreview() {
    RecommendationGrid()
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

@Preview(showBackground = false)
@Composable
fun BottomNavBarPreview() {
    BottomNavBar()
}
