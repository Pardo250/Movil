package com.example.condorapp.ui.userprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.condorapp.data.Review
import com.example.condorapp.data.UserInfo
import com.example.condorapp.ui.theme.CondorStarActive
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de perfil de otro usuario.
 * Muestra datos del usuario y sus reviews.
 */
@Composable
fun UserProfileScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onFollowListClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    UserProfileScreenContent(
        state = uiState,
        modifier = modifier,
        onBack = onBack,
        onFollowToggle = viewModel::toggleFollow,
        onFollowListClick = onFollowListClick
    )
}

@Composable
fun UserProfileScreenContent(
    state: UserProfileUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onFollowToggle: () -> Unit,
    onFollowListClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // Top Bar
            item {
                Spacer(modifier = Modifier.height(24.dp))
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // User Info Header
            item {
                state.user?.let { user ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Avatar con la primera letra del nombre
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                user.nombre.first().uppercase(),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = user.nombre,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = colorScheme.outline
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = user.email,
                                fontSize = 14.sp,
                                color = colorScheme.outline
                            )
                        }
                        
                        Spacer(Modifier.height(16.dp))

                        // Contadores clickeables
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { onFollowListClick(user.id) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text("${state.followersCount}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colorScheme.onBackground)
                                Text("Seguidores", color = colorScheme.outline, fontSize = 14.sp)
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable { onFollowListClick(user.id) }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text("${state.followingCount}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = colorScheme.onBackground)
                                Text("Siguiendo", color = colorScheme.outline, fontSize = 14.sp)
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        
                        // Botón Seguir / Siguiendo
                        Button(
                            onClick = onFollowToggle,
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.isFollowing) colorScheme.surfaceVariant else colorScheme.primary,
                                contentColor = if (state.isFollowing) colorScheme.onSurfaceVariant else colorScheme.onPrimary
                            ),
                            modifier = Modifier.width(200.dp)
                        ) {
                            Text(
                                text = if (state.isFollowing) "Siguiendo" else "Seguir",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } ?: run {
                    if (!state.isLoading) {
                        Text(
                            "Usuario no encontrado",
                            color = colorScheme.error,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Sección de Reviews
            item {
                Text(
                    text = "Reviews de ${state.user?.nombre ?: "este usuario"}",
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
                        text = "Este usuario aún no ha hecho reseñas.",
                        color = colorScheme.outline,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

            items(state.reviews) { review ->
                UserReviewItem(review = review)
            }
        }
    }
}

/** Ítem de reseña del usuario con calificación y comentario. */
@Composable
fun UserReviewItem(review: Review, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.name, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
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
            Spacer(Modifier.height(8.dp))
            Text(
                review.comment,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true, name = "UserProfile - Light")
@Composable
fun UserProfileScreenPreview() {
    CondorappTheme(darkTheme = false) {
        UserProfileScreenContent(
            state = UserProfileUiState(
                user = UserInfo("1", "Juan Pérez", "juan@email.com"),
                isFollowing = false,
                followersCount = 120,
                followingCount = 45,
                reviews = listOf(
                    Review("1", "Juan Pérez", 5, "Muy bueno", 2),
                    Review("2", "Juan Pérez", 3, "Regular", 0)
                )
            ),
            onBack = {},
            onFollowToggle = {},
            onFollowListClick = {}
        )
    }
}
