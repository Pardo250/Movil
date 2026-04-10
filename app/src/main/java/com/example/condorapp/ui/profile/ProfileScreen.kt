@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.condorapp.R
import com.example.condorapp.data.Review
import com.example.condorapp.ui.components.ProfileImage
import com.example.condorapp.ui.theme.CondorStarActive
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de perfil. Conecta el ProfileViewModel con el contenido
 * stateless. Incluye editar/eliminar reviews propias.
 */
@Composable
fun ProfileScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = uiState,
        modifier = modifier,
        onBack = onBack,
        onEditProfile = onEditProfile,
        onShareProfile = { /* Lógica para compartir */ },
        onEditReview = viewModel::startEditReview,
        onDeleteReview = viewModel::deleteReview
    )

    // Diálogo de edición de review propia
    if (uiState.isEditingReview) {
        EditReviewDialog(
            comment = uiState.editComment,
            rating = uiState.editRating,
            onCommentChange = viewModel::onEditCommentChange,
            onRatingChange = viewModel::onEditRatingChange,
            onConfirm = viewModel::confirmEditReview,
            onDismiss = viewModel::cancelEditReview
        )
    }
}

/** Contenido stateless de la pantalla de perfil. */
@Composable
fun ProfileScreenContent(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onShareProfile: () -> Unit,
    onEditReview: (Review) -> Unit,
    onDeleteReview: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { ProfileTopBar(onBack = onBack) }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item { ProfileHeader(name = state.name, username = state.username, imageUrl = state.imageUrl) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { ProfileActions(onEditProfile = onEditProfile, onShareProfile = onShareProfile) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Text(
                    text = "Mis Reseñas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            if (state.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colorScheme.primary)
                    }
                }
            }

            state.errorMessage?.let { error ->
                item {
                    Text(text = error, color = colorScheme.error, modifier = Modifier.padding(vertical = 8.dp))
                }
            }

            if (!state.isLoading && state.reviews.isEmpty() && state.errorMessage == null) {
                item {
                    Text(
                        text = "Aún no has hecho reseñas.",
                        color = colorScheme.outline,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            items(state.reviews) { review ->
                MyReviewItem(
                    review = review,
                    onEdit = { onEditReview(review) },
                    onDelete = { onDeleteReview(review.id) }
                )
            }
        }
    }
}

/** Barra superior con botón de retroceso. */
@Composable
fun ProfileTopBar(modifier: Modifier = Modifier, onBack: () -> Unit) {
    IconButton(onClick = onBack, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

/** Header del perfil con avatar, nombre y username. */
@Composable
fun ProfileHeader(modifier: Modifier = Modifier, name: String, username: String, imageUrl: String?) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        ProfileImage(
            imageUrl = imageUrl,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
        Text(text = username, fontSize = 16.sp, color = colorScheme.outline)
    }
}

/** Botones de acción del perfil: editar y compartir. */
@Composable
fun ProfileActions(
    modifier: Modifier = Modifier,
    onEditProfile: () -> Unit,
    onShareProfile: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = onEditProfile,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) { Text(stringResource(R.string.edit_profile), fontWeight = FontWeight.Bold) }
        OutlinedButton(
            onClick = onShareProfile,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors =
            ButtonDefaults.outlinedButtonColors(contentColor = colorScheme.onBackground)
        ) { Text(stringResource(R.string.share_profile), fontWeight = FontWeight.Bold) }
    }
}

/**
 * Ítem de reseña propia con calificación, comentario, nombre del artículo,
 * y botones de editar/eliminar.
 */
@Composable
fun MyReviewItem(
    review: Review,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre del artículo donde se hizo la review
            if (review.articuloNombre.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "📌 ${review.articuloNombre}",
                        color = colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
            }

            // Rating con estrellas
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Reseña #${review.id}", fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < review.rating) CondorStarActive
                                       else colorScheme.outlineVariant,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Comentario
            Spacer(Modifier.height(8.dp))
            Text(
                review.comment,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Botones de editar y eliminar (solo para reviews propias)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Editar", fontSize = 12.sp)
                }
                TextButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colorScheme.error
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar", fontSize = 12.sp, color = colorScheme.error)
                }
            }
        }
    }
}

/** Diálogo para editar una review existente. */
@Composable
fun EditReviewDialog(
    comment: String,
    rating: Int,
    onCommentChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Reseña", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Calificación", fontWeight = FontWeight.Bold, color = colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = { onRatingChange(star) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (star <= rating) CondorStarActive
                                       else colorScheme.outlineVariant,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Comentario", fontWeight = FontWeight.Bold, color = colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = onCommentChange,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Escribe tu reseña...") }
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, name = "Profile - Light")
@Composable
fun ProfileScreenPreviewLight() {
    CondorappTheme(darkTheme = false) {
        ProfileScreenContent(
            state = ProfileUiState(
                name = "Preview User",
                username = "@preview",
                imageUrl = null,
                reviews = listOf(
                    Review("1", "Yo", 5, "Excelente lugar", 3, articuloNombre = "Valle del Cocora")
                )
            ),
            onBack = {},
            onEditProfile = {},
            onShareProfile = {},
            onEditReview = {},
            onDeleteReview = {}
        )
    }
}

@Preview(showBackground = true, name = "Profile - Dark")
@Composable
fun ProfileScreenPreviewDark() {
    CondorappTheme(darkTheme = true) {
        ProfileScreenContent(
            state = ProfileUiState(
                name = "Preview User",
                username = "@preview",
                imageUrl = null,
                reviews = emptyList()
            ),
            onBack = {},
            onEditProfile = {},
            onShareProfile = {},
            onEditReview = {},
            onDeleteReview = {}
        )
    }
}
