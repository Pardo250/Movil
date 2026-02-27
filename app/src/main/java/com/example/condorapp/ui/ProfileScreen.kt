@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavHostController
import com.example.condorapp.R
import com.example.condorapp.data.UserProfile
import com.example.condorapp.data.local.UserProfileRepository
import com.example.condorapp.ui.theme.CondorappTheme

data class ProfileUiState(
    val name: String = "",
    val username: String = "",
    val photos: List<Int> = emptyList()
)

@Composable
fun ProfileScreenRoute(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val profile = remember { UserProfileRepository.getProfile() }
    val state by remember {
        mutableStateOf(
            ProfileUiState(
                name = profile.name,
                username = profile.username,
                photos = profile.photos
            )
        )
    }

    ProfileScreenContent(
        state = state,
        navController = navController,
        modifier = modifier,
        onBack = onBack,
        onEditProfile = { navController.navigate(Destinos.EDIT_PROFILE) },
        onShareProfile = { /* Lógica para compartir */ }
    )
}

@Composable
fun ProfileScreenContent(
    state: ProfileUiState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
    onShareProfile: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            ProfileTopBar(onBack = onBack)
            Spacer(modifier = Modifier.height(20.dp))
            ProfileHeader(name = state.name, username = state.username)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileActions(onEditProfile = onEditProfile, onShareProfile = onShareProfile)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileTabs()
            Spacer(modifier = Modifier.height(16.dp))
            PhotoGrid(photos = state.photos, modifier = Modifier.weight(1f))
        }

        BottomFloatingBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
fun ProfileTopBar(modifier: Modifier = Modifier, onBack: () -> Unit) {
    IconButton(onClick = onBack, modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Regresar",
            tint = Color.Black
        )
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
        Text(
            text = name,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = DesignGreenDark
        )
        Text(
            text = username,
            fontSize = 16.sp,
            color = Color.Gray
        )
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
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DesignGreenDark)
        ) {
            Text("Editar Perfil", fontWeight = FontWeight.Bold)
        }
        OutlinedButton(
            onClick = onShareProfile,
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
        ) {
            Text("Compartir", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileTabs(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth()
            .background(Color.White.copy(0.5f), RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp)
    ) {
        TabItem(icon = Icons.Default.Edit, label = "Reseñas")
        TabItem(icon = Icons.Default.ChatBubble, label = "Comentarios")
        TabItem(icon = Icons.Default.Bookmark, label = "Interés")
    }
}

@Composable
fun TabItem(modifier: Modifier = Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Icon(icon, contentDescription = null, tint = DesignGreenDark, modifier = Modifier.size(28.dp))
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun PhotoGrid(photos: List<Int>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        items(photos) { photo ->
            Image(
                painter = painterResource(photo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }
}