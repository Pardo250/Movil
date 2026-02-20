package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
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
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

data class ProfileUiState(
    val name: String = "Camilo Jiménez",
    val username: String = "@Camilo_co",
    val photos: List<Int> = listOf(
        R.drawable.cartagena,
        R.drawable.valle_del_cocora,
        R.drawable.medellin,
        R.drawable.santamarta,
        R.drawable.catedral,
        R.drawable.atardecer
    )
)

@Composable
fun ProfileScreenRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onShareProfile: () -> Unit = {}
) {
    val state by remember { mutableStateOf(ProfileUiState()) }

    ProfileScreenContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onEditProfile = onEditProfile,
        onShareProfile = onShareProfile
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onShareProfile: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        ProfileTopBar(onBack = onBack)
        Spacer(modifier = Modifier.height(20.dp))
        ProfileHeader(name = state.name, username = state.username)
        Spacer(modifier = Modifier.height(20.dp))
        ProfileActions(onEditProfile = onEditProfile, onShareProfile = onShareProfile)
        Spacer(modifier = Modifier.height(24.dp))
        ProfileTabs()
        Spacer(modifier = Modifier.height(16.dp))
        PhotoGrid(photos = state.photos, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ProfileTopBar(modifier: Modifier = Modifier, onBack: () -> Unit) {
    IconButton(onClick = onBack, modifier = modifier) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
    }
}

@Composable
fun ProfileHeader(modifier: Modifier = Modifier, name: String, username: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(text = username, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
fun ProfileActions(modifier: Modifier = Modifier, onEditProfile: () -> Unit, onShareProfile: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onEditProfile,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Editar Perfil")
        }
        OutlinedButton(
            onClick = onShareProfile,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Compartir")
        }
    }
}

@Composable
fun ProfileTabs(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
    ) {
        TabItem(icon = Icons.Default.Edit, label = "Reseñas")
        TabItem(icon = Icons.Default.ChatBubble, label = "Comentarios")
        TabItem(icon = Icons.Default.Bookmark, label = "Interés")
    }
}

@Composable
fun TabItem(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun PhotoGrid(photos: List<Int>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(photos) { photo ->
            Image(
                painter = painterResource(photo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CondorappTheme { ProfileScreenRoute() }
}
