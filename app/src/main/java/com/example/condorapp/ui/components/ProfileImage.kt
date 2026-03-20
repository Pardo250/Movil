package com.example.condorapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Gray,
    borderWidth: Float = 2f
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(borderWidth.dp, borderColor, CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Placeholder de perfil",
                tint = Color.White,
                modifier = Modifier.fillMaxSize(0.6f)
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de perfil del usuario",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
