@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.condorapp.R
import com.example.condorapp.ui.components.ProfileImage
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de perfil. Conecta el ProfileViewModel con el contenido
 * stateless.
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
        onShareProfile = { /* Lógica para compartir */ }
    )
}

/** Contenido stateless de la pantalla de perfil. */
@Composable
fun ProfileScreenContent(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onShareProfile: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(modifier = modifier.fillMaxSize().background(colorScheme.background)) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileTopBar(onBack = onBack)
            Spacer(modifier = Modifier.height(20.dp))
            ProfileHeader(name = state.name, username = state.username, imageUrl = state.imageUrl)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileActions(onEditProfile = onEditProfile, onShareProfile = onShareProfile)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileTabs()
            Spacer(modifier = Modifier.height(16.dp))
            PhotoGrid(photos = state.photos, modifier = Modifier.weight(1f))
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

/** Tabs del perfil: Reseñas, Comentarios, Interés. */
@Composable
fun ProfileTabs(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier =
        modifier.fillMaxWidth()
            .background(
                colorScheme.surfaceVariant.copy(0.5f),
                RoundedCornerShape(16.dp)
            )
            .padding(vertical = 12.dp)
    ) {
        TabItem(icon = Icons.Default.Edit, label = "Reseñas")
        TabItem(icon = Icons.Default.ChatBubble, label = "Comentarios")
        TabItem(icon = Icons.Default.Bookmark, label = "Interés")
    }
}

/** Ítem individual de tab del perfil. */
@Composable
fun TabItem(modifier: Modifier = Modifier, icon: ImageVector, label: String) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(
            icon,
            contentDescription = null,
            tint = colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )
    }
}

/** Grid de fotos del perfil. */
@Composable
fun PhotoGrid(photos: List<String>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(photos) { photo ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(16.dp))
            )
        }
    }
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
                photos = emptyList()
            ),
            onBack = {},
            onEditProfile = {},
            onShareProfile = {}
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
                photos = emptyList()
            ),
            onBack = {},
            onEditProfile = {},
            onShareProfile = {}
        )
    }
}
