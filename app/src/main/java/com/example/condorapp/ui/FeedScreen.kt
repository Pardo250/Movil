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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.condorapp.R

@Composable
fun FeedScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.Olivafeed))
    ) {

        Column (
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
                text = "Recomendado para ti",
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


@Composable
@Preview (showBackground = true)
fun FeedScreenPreview() {
    FeedScreen()
}


@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFD9D2C7)),
        contentAlignment = Alignment.CenterStart
    ) {
        Row (
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Buscar...", color = Color.Gray)
        }
    }
}

@Composable
@Preview (showBackground = false)
fun SearchBarPreview() {
    SearchBar()
}

@Composable
fun MapCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        elevation = CardDefaults.cardElevation(8.dp)
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
@Preview (showBackground = false)
fun MapCardPreview() {
    MapCard()
}


@Composable
fun CategoryChips() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        FilterChip("Paisaje", true)
        FilterChip("Playas", false)
        FilterChip("Cultural", false)
        FilterChip("Hoteles", false)
    }
}

@Composable
@Preview (showBackground = false)
fun CategoryChipsPreview() {
    CategoryChips()
}


@Composable
fun FilterChip(text: String, selected: Boolean) {

    val background =
        if (selected) Color(0xFF3E5F2C)
        else Color(0xFF9FB18A)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
@Preview (showBackground = true)
fun FilterChipPreview() {
    FilterChip("Paisaje", true)
}

@Composable
fun RecommendationGrid() {

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

    LazyVerticalGrid (
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(320.dp)
    ) {
        items (places) { image ->
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}

@Composable
@Preview (showBackground = false)
fun RecommendationGridPreview() {
    RecommendationGrid()
}

@Composable
fun BottomNavBar() {
    Card(
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Icon(Icons.Default.Home, contentDescription = null)
            Icon(Icons.Default.Person, contentDescription = null)
        }
    }
}

@Composable
@Preview (showBackground = false)
fun BottomNavBarPreview() {
    BottomNavBar()
}

