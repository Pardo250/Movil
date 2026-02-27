@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.condorapp.data.Review
import com.example.condorapp.data.local.ReviewRepository
import com.example.condorapp.ui.theme.CondorappTheme

data class DetailUiState(
    val title: String = "Valle del Cocora",
    val location: String = "Eje Cafetero",
    val description: String = "Imagina caminar entre las palmas más altas del mundo, donde la niebla abraza las montañas verdes de Colombia.",
    val imageRes: Int = R.drawable.valle_del_cocora,
    val reviews: List<Review> = emptyList()
)

@Composable
fun DetailScreen(
    postId: String,
    onBack: () -> Unit,
    onAddReview: () -> Unit
) {
    val initialReviews = remember { ReviewRepository.getReviews() }
    var state by remember { mutableStateOf(DetailUiState(reviews = initialReviews, title = postId)) }

    DetailScreenContent(
        state = state,
        onBackClick = onBack,
        onAddReviewClick = onAddReview,
        onLikeReview = { review ->
            val updatedReviews = state.reviews.map {
                if (it == review) it.copy(likes = it.likes + 1) else it
            }
            state = state.copy(reviews = updatedReviews)
        }
    )
}

@Composable
fun DetailScreenContent(
    state: DetailUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onAddReviewClick: () -> Unit,
    onLikeReview: (Review) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
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
                    description = state.description,
                    onAddReviewClick = onAddReviewClick
                )
            }

            item {
                Text(
                    text = "Reseñas de la comunidad",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
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
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
                .background(
                    color = colorScheme.surface.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = colorScheme.onSurface
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = colorScheme.surface.copy(alpha = 0.9f),
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    color = colorScheme.onSurface,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Destino Popular",
                        color = colorScheme.outline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun DetailInfoSection(
    location: String,
    description: String,
    onAddReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = location,
            style = MaterialTheme.typography.headlineSmall,
            color = colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onBackground.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AssistChip(
                onClick = onAddReviewClick,
                label = { Text("Añadir Reseña") },
                leadingIcon = { Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp)) },
                colors = AssistChipDefaults.assistChipColors(labelColor = colorScheme.primary)
            )
            AssistChip(
                onClick = { },
                label = { Text("Precios") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null, modifier = Modifier.size(18.dp)) }
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier, onLike: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        review.name.first().toString(),
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        review.name,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    )
                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < review.rating) Color(0xFFFFB400) else colorScheme.outlineVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                IconButton(onClick = onLike) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            review.likes.toString(),
                            color = colorScheme.outline,
                            fontSize = 14.sp
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = colorScheme.primary
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                review.comment,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true, name = "Detail - Light")
@Composable
fun DetailScreenPreviewLight() {
    CondorappTheme(darkTheme = false) {
        DetailScreen(postId = "Valle del Cocora", onBack = {}, onAddReview = {})
    }
}

@Preview(showBackground = true, name = "Detail - Dark")
@Composable
fun DetailScreenPreviewDark() {
    CondorappTheme(darkTheme = true) {
        DetailScreen(postId = "Valle del Cocora", onBack = {}, onAddReview = {})
    }
}
