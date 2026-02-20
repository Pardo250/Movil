@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

// Entidad de UI (Datos quemados locales)
data class Post(
    val user: String,
    val location: String,
    val imageRes: Int,
    val likes: String,
    val comments: String
)

data class HomeUiState(
    val posts: List<Post> = listOf(
        Post("Alejandra Gomez", "Valle del Cocora", R.drawable.valle_del_cocora, "1.2k", "58"),
        Post("Mateo Ruiz", "Cartagena Old City", R.drawable.cartagena, "980", "30"),
        Post("Sofia Castro", "Sierra Nevada", R.drawable.santamarta, "2.5k", "120")
    ),
    val selectedPostIndex: Int? = null
)

@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNotifications: () -> Unit = {}
) {
    var state by remember { mutableStateOf(HomeUiState()) }

    HomeScreenContent(
        state = state,
        modifier = modifier,
        onPostClick = { index ->
            state = state.copy(selectedPostIndex = index)
            println("Post seleccionado: ${state.posts[index].user}") // Print solicitado
        }
    )
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onPostClick: (Int) -> Unit
) {
    // YA NO HAY SCAFFOLD AQUÍ. Solo el contenido.
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp) // Espacio para el BottomBar global
    ) {
        item {
            HeaderSection(modifier = Modifier.padding(16.dp))
        }

        itemsIndexed(state.posts) { index, post ->
            PostCard(
                post = post,
                isSelected = state.selectedPostIndex == index,
                onClick = { onPostClick(index) }
            )
        }
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(12.dp))
        FilterBar()
    }
}

@Composable
fun FilterBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(40.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(painter = painterResource(R.drawable.ic_menu), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text = "Filtros", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Icon(painter = painterResource(R.drawable.ic_logo), contentDescription = null, tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun PostCard(
    post: Post,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(26.dp),
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
                    Text(post.user.first().toString(), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(post.user, fontWeight = FontWeight.Bold)
                    Text(post.location, style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Image(
                painter = painterResource(post.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )

            Text(
                text = stringResource(R.string.post_lorem),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(R.drawable.ic_heart), contentDescription = null, modifier = Modifier.size(20.dp))
                Text(post.likes, modifier = Modifier.padding(start = 4.dp))
                Spacer(Modifier.width(20.dp))
                Icon(painter = painterResource(R.drawable.ic_comment), contentDescription = null, modifier = Modifier.size(20.dp))
                Text(post.comments, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    CondorappTheme {
        HomeScreenRoute()
    }
}
