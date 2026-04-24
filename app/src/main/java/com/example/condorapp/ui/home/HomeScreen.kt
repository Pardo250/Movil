@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.home

import androidx.compose.foundation.background
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.condorapp.R
import com.example.condorapp.data.Articulo
import com.example.condorapp.data.Review
import com.example.condorapp.ui.theme.CondorStarActive
import com.example.condorapp.ui.theme.CondorappTheme

/** Composable Route para la pantalla Home. Conecta el HomeViewModel con el contenido stateless. */
@Composable
fun HomeScreenRoute(
        modifier: Modifier = Modifier,
        viewModel: HomeViewModel = hiltViewModel(),
        onPostClick: (String) -> Unit,
        onNotificationsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
            state = uiState,
            modifier = modifier,
            onNotifications = onNotificationsClick,
            onToggleFilter = viewModel::onToggleFilter,
            onArticuloClick = { index ->
                viewModel.onArticuloSelected(index)
                val articulo = uiState.articulos[index]
                onPostClick(articulo.id)
            }
    )
}

/** Contenido stateless de la pantalla Home con lista de artículos del backend. */
@Composable
fun HomeScreenContent(
        state: HomeUiState,
        modifier: Modifier = Modifier,
        onNotifications: () -> Unit,
        onToggleFilter: (Boolean) -> Unit,
        onArticuloClick: (Int) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
        LazyColumn(contentPadding = PaddingValues(bottom = 100.dp)) {
            item { HomeHeader(onNotifications = onNotifications) }

            // Toggle Todos / Siguiendo
            item {
                HomeToggle(
                    showFollowingOnly = state.showFollowingOnly,
                    onToggle = onToggleFilter,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
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
                    Text(
                        text = error,
                        color = colorScheme.error,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
            }

            if (state.showFollowingOnly) {
                // Modo Siguiendo: mostrar reviews de usuarios seguidos
                if (state.reviews.isEmpty() && !state.isLoading) {
                    item {
                        Text(
                            text = "No hay reseñas de usuarios que sigues aún.",
                            color = colorScheme.outline,
                            modifier = Modifier.padding(20.dp)
                        )
                    }
                }
                items(state.reviews, key = { it.id }) { review ->
                    ReviewCardHome(review = review)
                }
            } else {
                // Modo Todos: mostrar artículos
                itemsIndexed(state.articulos) { index, articulo ->
                    ArticuloCard(
                            articulo = articulo,
                            isSelected = state.selectedIndex == index,
                            onClick = { onArticuloClick(index) }
                    )
                }
            }
        }
    }
}

/** Header de Home con botón atrás, avatar y notificaciones. */
@Composable
fun HomeHeader(modifier: Modifier = Modifier, onNotifications: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                    modifier =
                            Modifier.size(40.dp)
                                    .background(colorScheme.primary.copy(alpha = 0.4f), CircleShape)
                                    .clickable { /* Acción atrás */},
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = colorScheme.onSurface,
                        modifier = Modifier.size(20.dp)
                )
            }
            ProfileAvatar()
            IconButton(onClick = onNotifications) {
                Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        tint = colorScheme.onSurface
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        FilterBar()
        Spacer(Modifier.height(20.dp))
    }
}

/** Avatar circular del perfil del usuario. */
@Composable
fun ProfileAvatar(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            modifier = modifier.size(80.dp).clip(CircleShape)
    )
}

/** Barra de filtros con logo y texto. */
@Composable
fun FilterBar(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
            modifier =
                    modifier.width(280.dp)
                            .height(54.dp)
                            .background(colorScheme.surfaceVariant, RoundedCornerShape(27.dp))
                            .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
                painter = painterResource(R.drawable.ic_menu),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(24.dp)
        )
        Text(
                stringResource(R.string.filter),
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurfaceVariant,
                fontSize = 18.sp
        )
        Box(
                modifier =
                        Modifier.size(40.dp)
                                .background(colorScheme.primary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(24.dp)
            )
        }
    }
}

/** Tarjeta de un artículo del backend con imagen, título, descripción y tipo. */
@Composable
fun ArticuloCard(articulo: Articulo, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
            shape = RoundedCornerShape(32.dp),
            modifier =
                    modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                            .fillMaxWidth()
                            .clickable { onClick() },
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    if (isSelected) colorScheme.primaryContainer
                                    else colorScheme.surface
                    ),
            elevation = CardDefaults.cardElevation(if (isSelected) 2.dp else 8.dp)
    ) {
        Column {
            // Imagen del artículo
            if (articulo.imagenUrl.isNotBlank()) {
                AsyncImage(
                    model = articulo.imagenUrl,
                    contentDescription = articulo.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                )
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .background(colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            articulo.tipo.first().uppercase(),
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            articulo.titulo,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                articulo.tipo,
                                color = colorScheme.primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                if (articulo.descripcion.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = articulo.descripcion,
                        color = colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

/** Toggle Todos / Siguiendo para el Home. */
@Composable
fun HomeToggle(
    showFollowingOnly: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.surfaceVariant, RoundedCornerShape(30.dp))
            .padding(4.dp)
    ) {
        listOf(false, true).forEach { isFollowing ->
            val isSelected = showFollowingOnly == isFollowing
            val label = if (isFollowing) "Siguiendo" else "Todos"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (isSelected) colorScheme.primary else colorScheme.surfaceVariant,
                        RoundedCornerShape(30.dp)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .clickable { onToggle(isFollowing) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    label,
                    color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/** Tarjeta compacta de review para el feed del Home. */
@Composable
fun ReviewCardHome(review: Review, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        review.name.firstOrNull()?.toString() ?: "?",
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(review.name, fontWeight = FontWeight.Bold, color = colorScheme.onSurface, fontSize = 14.sp)
                    if (review.articuloNombre.isNotBlank()) {
                        Text("en ${review.articuloNombre}", color = colorScheme.outline, fontSize = 12.sp)
                    }
                }
                Row {
                    repeat(5) { i ->
                        Icon(
                            Icons.Default.Star, null,
                            tint = if (i < review.rating) CondorStarActive else colorScheme.outlineVariant,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            if (review.comment.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(review.comment, style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}

@Preview(showBackground = true, name = "Home - Light")
@Composable
fun HomeScreenLightPreview() {
    CondorappTheme(darkTheme = false) {
        HomeScreenContent(
            state = HomeUiState(
                articulos = listOf(
                    Articulo("1", "Artículo de prueba", "Descripción de prueba", "cultura")
                )
            ),
            onNotifications = {},
            onToggleFilter = {},
            onArticuloClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Home - Dark")
@Composable
fun HomeScreenDarkPreview() {
    CondorappTheme(darkTheme = true) {
        HomeScreenContent(
            state = HomeUiState(
                articulos = listOf(
                    Articulo("1", "Artículo de prueba", "Descripción de prueba", "cultura")
                )
            ),
            onNotifications = {},
            onToggleFilter = {},
            onArticuloClick = {}
        )
    }
}
