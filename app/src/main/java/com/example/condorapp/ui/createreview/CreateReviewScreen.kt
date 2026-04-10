package com.example.condorapp.ui.createreview

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de creación de reseñas. Conecta el CreateReviewViewModel con el
 * contenido stateless.
 */
@Composable
fun CreateReviewScreenRoute(
        modifier: Modifier = Modifier,
        articuloId: Int = 1,
        viewModel: CreateReviewViewModel = hiltViewModel(),
        onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navegar atrás automáticamente cuando la creación es exitosa
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.onNavigationHandled()
            onBackClick()
        }
    }

    CreateReviewScreenContent(
            state = uiState,
            modifier = modifier,
            onBackClick = onBackClick,
            onRatingChange = viewModel::onRatingChange,
            onCommentChange = viewModel::onCommentChange,
            onPublish = { viewModel.onPublish(articuloId) }
    )
}

/** Contenido stateless de la pantalla de creación de reseñas. */
@Composable
fun CreateReviewScreenContent(
        state: CreateReviewUiState,
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit,
        onRatingChange: (Int) -> Unit,
        onCommentChange: (String) -> Unit,
        onPublish: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            CreateReviewBackButton(onClick = onBackClick)
            Spacer(modifier = Modifier.height(16.dp))
            CreateReviewTitle(text = state.title)
            Spacer(modifier = Modifier.height(16.dp))
            CreateReviewDescription()
            Spacer(modifier = Modifier.height(32.dp))
            CreateReviewRatingSection(rating = state.rating, onRatingChange = onRatingChange)
            Spacer(modifier = Modifier.height(32.dp))
            CreateReviewImageUploadSection()
            Spacer(modifier = Modifier.height(24.dp))
            CreateReviewCommentField(comment = state.comment, onCommentChange = onCommentChange)
            Spacer(modifier = Modifier.height(100.dp))
        }
        CreateReviewPublishButton(
                modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp),
                onClick = onPublish
        )
    }
}

/** Botón circular de retroceso. */
@Composable
fun CreateReviewBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
            onClick = onClick,
            modifier = modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/** Título de la sección de creación de reseña. */
@Composable
fun CreateReviewTitle(text: String, modifier: Modifier = Modifier) {
    Text(
            text = text,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            lineHeight = 44.sp,
            modifier = modifier
    )
}

/** Descripción introductoria de la sección. */
@Composable
fun CreateReviewDescription(modifier: Modifier = Modifier) {
    Text(
            text = "¡Bienvenido de vuelta! Para nosotros, nada es más valioso que tu perspectiva.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = modifier.fillMaxWidth()
    )
}

/** Sección de calificación con estrellas interactivas. */
@Composable
fun CreateReviewRatingSection(
        modifier: Modifier = Modifier,
        rating: Int,
        onRatingChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Text(
                text = "TU CALIFICACIÓN",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(5) { index ->
                Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint =
                                if (index < rating) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(40.dp).clickable { onRatingChange(index + 1) }
                )
            }
        }
    }
}

/** Sección de subida de imagen. */
@Composable
fun CreateReviewImageUploadSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
                "Añadir imagen",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(
                modifier =
                        Modifier.size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline,
                                        RoundedCornerShape(16.dp)
                                ),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/** Campo de comentario para la reseña. */
@Composable
fun CreateReviewCommentField(
        modifier: Modifier = Modifier,
        comment: String,
        onCommentChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
                "Comentario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
                value = comment,
                onValueChange = onCommentChange,
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Cuéntanos más...") }
        )
    }
}

/** Botón de publicar reseña flotante en la parte inferior. */
@Composable
fun CreateReviewPublishButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
            onClick = onClick,
            modifier = modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp)
    ) { Text("Publicar Reseña", style = MaterialTheme.typography.titleMedium) }
}

@Preview(showBackground = true, name = "Create Review - Light")
@Composable
fun CreateReviewScreenPreviewLight() {
    CondorappTheme(darkTheme = false) { CreateReviewScreenRoute() }
}

@Preview(showBackground = true, name = "Create Review - Dark")
@Composable
fun CreateReviewScreenPreviewDark() {
    CondorappTheme(darkTheme = true) { CreateReviewScreenRoute() }
}
