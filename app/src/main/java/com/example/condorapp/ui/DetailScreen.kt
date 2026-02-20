package com.example.condorapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R.drawable

val LightGreenBackground = Color(0xFFEFF5EA)
val SoftGreen = Color(0xFF7C9A6D)
val DarkGreen = Color(0xFF4F7336)
val StarColor = Color(0xFFBFAE9F)

data class Review(
    val name: String,
    val rating: Int,
    val comment: String,
    val likes: Int
)

@Composable
fun DetailScreen(onBackClick: () -> Unit) {

    var selectedTab by remember { mutableStateOf(1) }

    var reviews by remember {
        mutableStateOf(
            listOf(
                Review("Maria Valen", 5, "Category • $$ • 1.2 miles away\nSupporting line text lorem ipsum...", 0),
                Review("Maria Valen", 5, "Category • $$ • 1.2 miles away\nSupporting line text lorem ipsum...", 0),
                Review("Maria Valen", 5, "Category • $$ • 1.2 miles away\nSupporting line text lorem ipsum...", 0)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreenBackground)
    ) {

        LazyColumn {

            item {
                Box {

                    Image(
                        painter = painterResource(id = drawable.valle_del_cocora),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )

                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(48.dp)
                            .background(SoftGreen, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            "Valle del Cocora",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Subtitle",
                            color = Color.White.copy(0.9f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row {

                            ActionButton("Agregar como Interes")
                            Spacer(modifier = Modifier.width(8.dp))
                            ActionButton("12 mins from hotel")
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LightGreenBackground)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Eje Cafetero", fontSize = 24.sp)
                    Text("Within 5 miles • $$-$$$")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Imagina caminar entre las palmas más altas del mundo...",
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGreen)
                        ) {
                            Icon(Icons.Default.Check, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Tu experiencia")
                        }

                        Spacer(Modifier.width(12.dp))

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(30.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoftGreen)
                        ) {
                            Text("Precios")
                        }
                    }
                }
            }

            items(reviews) { review ->

                ReviewItem(review) {
                    reviews = reviews.map {
                        if (it == review) it.copy(likes = it.likes + 1) else it
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // BOTTOM FLOATING BAR
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .clip(RoundedCornerShape(40.dp))
                .background(Color.White)
                .padding(horizontal = 40.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            BottomIcon(Icons.Default.LocationOn, "Explore", selectedTab == 0) { selectedTab = 0 }
            Spacer(Modifier.width(40.dp))
            BottomIcon(Icons.Default.Home, "Home", selectedTab == 1, true) { selectedTab = 1 }
            Spacer(Modifier.width(40.dp))
            BottomIcon(Icons.Default.Person, "Profile", selectedTab == 2) { selectedTab = 2 }
        }
    }
}

@Composable
fun ActionButton(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xAA4F7336))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = Color.White)
    }
}

@Composable
fun ReviewItem(review: Review, onLike: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE57373))
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(review.name, fontWeight = FontWeight.Bold)

                Row {
                    repeat(review.rating) {
                        Icon(Icons.Default.Star, null, tint = StarColor)
                    }
                }

                Text(review.comment)
            }

            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.clickable { onLike() }
            )
        }

        Divider(
            modifier = Modifier.padding(top = 16.dp),
            color = SoftGreen.copy(alpha = 0.4f)
        )
    }
}

@Composable
fun BottomIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {

        Icon(
            icon,
            contentDescription = null,
            tint = if (selected) DarkGreen else Color.Gray
        )
        Text(label, fontSize = 12.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetail() {
    DetailScreen(onBackClick = {})
}