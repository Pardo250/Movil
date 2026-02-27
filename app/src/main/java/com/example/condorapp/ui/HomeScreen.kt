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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.condorapp.data.Post
import com.example.condorapp.data.local.PostRepository
import com.example.condorapp.ui.theme.CondorappTheme

data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val selectedPostIndex: Int? = null
)

@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    onPostClick: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {
    val posts = remember { PostRepository.getPosts() }
    var state by remember { mutableStateOf(HomeUiState(posts = posts)) }

    HomeScreenContent(
        state = state,
        modifier = modifier,
        onNotifications = onNotificationsClick,
        onPostClick = { index ->
            state = state.copy(selectedPostIndex = index)
            onPostClick(state.posts[index].location)
        }
    )
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onNotifications: () -> Unit,
    onPostClick: (Int) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                HomeHeader(onNotifications = onNotifications)
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
}

@Composable
fun HomeHeader(onNotifications: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón de atrás circular (como en la imagen)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colorScheme.primary.copy(alpha = 0.4f), CircleShape)
                    .clickable { /* Acción atrás */ },
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
                Icon(Icons.Default.Notifications, contentDescription = null, tint = colorScheme.onSurface)
            }
        }
        Spacer(Modifier.height(16.dp))
        FilterBar()
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun ProfileAvatar() {
    Image(
        painter = painterResource(R.drawable.avatar),
        contentDescription = null,
        modifier = Modifier.size(80.dp).clip(CircleShape)
    )
}

@Composable
fun FilterBar() {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .width(280.dp)
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
        
        // Logo con fondo circular
        Box(
            modifier = Modifier
                .size(40.dp)
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

@Composable
fun PostCard(post: Post, isSelected: Boolean, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colorScheme.primaryContainer else colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 2.dp else 8.dp)
    ) {
        Column {
            Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    post.user.first().toString(), 
                    fontWeight = FontWeight.Bold, 
                    color = colorScheme.primary, 
                    fontSize = 20.sp
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(post.user, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                    Text(post.location, color = colorScheme.outline, fontSize = 12.sp)
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
                color = colorScheme.onSurface.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, name = "Home - Light")
@Composable
fun HomeScreenLightPreview() {
    CondorappTheme(darkTheme = false) {
        HomeScreenRoute(onPostClick = {}, onNotificationsClick = {})
    }
}

@Preview(showBackground = true, name = "Home - Dark")
@Composable
fun HomeScreenDarkPreview() {
    CondorappTheme(darkTheme = true) {
        HomeScreenRoute(onPostClick = {}, onNotificationsClick = {})
    }
}
