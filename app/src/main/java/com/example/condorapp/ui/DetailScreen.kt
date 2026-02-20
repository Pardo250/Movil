package com.example.condorapp.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

data class Review(
    val name: String,
    val rating: Int,
    val comment: String,
    val likes: Int
)

data class DetailUiState(
    val title: String = "Valle del Cocora",
    val location: String = "Eje Cafetero",
    val description: String = "Imagina caminar entre las palmas más altas del mundo...",
    val imageRes: Int = R.drawable.valle_del_cocora,
    val reviews: List<Review> = listOf(
        Review("Maria Valen", 5, "Category • $$ • 1.2 miles away\nSupporting line text lorem ipsum...", 12),
        Review("Juan Perez", 4, "Un lugar increíble para visitar en familia.", 5),
        Review("Sofia Gomez", 5, "Las mejores vistas de Colombia.", 8)
    )
)

@Composable
fun DetailScreenRoute(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    var state by remember { mutableStateOf(DetailUiState()) }

    DetailScreenContent(
        state = state,
        modifier = modifier,
        onBackClick = onBackClick,
        onLikeReview = { review ->
            val updatedReviews = state.reviews.map {
                if (it == review) it.copy(likes = it.likes + 1) else it
            }
            state = state.copy(reviews = updatedReviews)
            println("Like en reseña de: ${review.name}")
        }
    )
}

@Composable
fun DetailScreenContent(
    state: DetailUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onLikeReview: (Review) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                DetailHeader(
                    title = state.title,
                    imageRes = state.imageRes,
                    onBackClick = onBackClick
                )
            }

            item {
                DetailInfoSection(
                    location = state.location,
                    description = state.description
                )
            }

            item {
                Text(
                    text = "Reseñas de la comunidad",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.reviews) { review ->
                ReviewItem(review = review, onLike = { onLikeReview(review) })
            }
        }
    }
}

@Composable
fun DetailHeader(
    title: String,
    imageRes: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = imageRes),
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
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Regresar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Destino Popular", color = Color.White)
            }
        }
    }
}

@Composable
fun DetailInfoSection(
    location: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = location, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AssistChip(
                onClick = { },
                label = { Text("Añadir Interés") },
                leadingIcon = { Icon(Icons.Default.Add, null) }
            )
            AssistChip(
                onClick = { },
                label = { Text("Precios") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null) }
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier, onLike: () -> Unit) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(review.name.first().toString(), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.name, fontWeight = FontWeight.Bold)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < review.rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                IconButton(onClick = onLike) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(review.likes.toString())
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.ThumbUp, contentDescription = null, modifier = Modifier.size(20.dp))
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(review.comment, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    CondorappTheme { DetailScreenRoute() }
}
