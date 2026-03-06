@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.condorapp.data.Review
import com.example.condorapp.ui.theme.CondorStarActive
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de detalle de un destino. Conecta el DetailViewModel con el
 * contenido stateless.
 */
@Composable
fun DetailScreenRoute(
        postId: String,
        modifier: Modifier = Modifier,
        viewModel: DetailViewModel = viewModel(),
        onBack: () -> Unit,
        onAddReview: () -> Unit,
        onReviewClick: (String) -> Unit
) {
    LaunchedEffect(postId) { viewModel.loadPostDetail(postId) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailScreenContent(
            state = uiState,
            modifier = modifier,
            onBackClick = onBack,
            onAddReviewClick = onAddReview,
            onReviewClick = onReviewClick,
            onLikeReview = viewModel::onLikeReview
    )
}

/** Contenido stateless de la pantalla de detalle. */
@Composable
fun DetailScreenContent(
        state: DetailUiState,
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit,
        onAddReviewClick: () -> Unit,
        onReviewClick: (String) -> Unit,
        onLikeReview: (Review) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
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
                ReviewItem(
                        review = review,
                        onLike = { onLikeReview(review) },
                        onClick = { onReviewClick(review.id) }
                )
            }
        }
    }
}

/** Header con imagen de fondo y botón de retroceso. */
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
                modifier = Modifier.fillMaxWidth().height(350.dp)
        )
        IconButton(
                onClick = onBackClick,
                modifier =
                        Modifier.padding(16.dp)
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
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
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

/** Sección de información del destino con descripción y acciones. */
@Composable
fun DetailInfoSection(
        location: String,
        description: String,
        onAddReviewClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
            modifier = modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
                text = "Post de $location",
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
                    leadingIcon = {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                    },
                    colors = AssistChipDefaults.assistChipColors(labelColor = colorScheme.primary)
            )
            AssistChip(
                    onClick = {},
                    label = { Text("Precios") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, null, modifier = Modifier.size(18.dp))
                    }
            )
        }
    }
}

/** Ítem de reseña con avatar, estrellas, comentario y like. */
@Composable
fun ReviewItem(
        review: Review,
        modifier: Modifier = Modifier,
        onLike: () -> Unit,
        onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
            modifier =
                    modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth().clickable {
                        onClick()
                    },
            shape = RoundedCornerShape(16.dp),
            colors =
                    CardDefaults.cardColors(
                            containerColor = colorScheme.surface,
                            contentColor = colorScheme.onSurface
                    ),
            elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                        modifier =
                                Modifier.size(45.dp)
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
                    Text(review.name, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint =
                                            if (index < review.rating) CondorStarActive
                                            else colorScheme.outlineVariant,
                                    modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                IconButton(onClick = onLike) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(review.likes.toString(), color = colorScheme.outline, fontSize = 14.sp)
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
        DetailScreenRoute(
                postId = "Valle del Cocora",
                onBack = {},
                onAddReview = {},
                onReviewClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Detail - Dark")
@Composable
fun DetailScreenPreviewDark() {
    CondorappTheme(darkTheme = true) {
        DetailScreenRoute(
                postId = "Valle del Cocora",
                onBack = {},
                onAddReview = {},
                onReviewClick = {}
        )
    }
}
