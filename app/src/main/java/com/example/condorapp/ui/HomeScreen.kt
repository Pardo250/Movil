@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
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
import com.example.condorapp.R
import com.example.condorapp.data.PostRepository
import com.example.condorapp.ui.theme.CondorappTheme
import androidx.compose.ui.graphics.Color

data class Post(
    val user: String,
    val location: String,
    val imageRes: Int,
    val likes: String,
    val comments: String
)

data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val selectedPostIndex: Int? = null
)

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNotifications: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(onClick = onNotifications) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.cd_notifications),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun ProfileAvatar(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.avatar),
        contentDescription = stringResource(R.string.cd_profile_photo),
        modifier = modifier
            .size(90.dp)
            .clip(CircleShape)
    )
}

@Composable
fun FilterBar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = stringResource(R.string.cd_menu),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(45.dp)
                .padding(start = 10.dp)
        )

        Spacer(Modifier.width(40.dp))

        Text(
            text = stringResource(R.string.filter),
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(Modifier.width(40.dp))

        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.cd_logo),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(45.dp)
                .padding(end = 10.dp)
        )
    }
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        ProfileAvatar()
        Spacer(Modifier.height(12.dp))
        FilterBar()
    }
}

@Composable
fun PostHeader(post: Post, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = post.user.first().toString(),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = post.user,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = post.location,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = stringResource(R.string.cd_more_options),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun PostImage(imageRes: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = stringResource(R.string.cd_post_image),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    )
}

@Composable
fun PostActions(likes: String, comments: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_heart),
            contentDescription = stringResource(R.string.cd_like),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = likes, color = MaterialTheme.colorScheme.onSurface)

        Spacer(Modifier.width(30.dp))

        Icon(
            painter = painterResource(R.drawable.ic_comment),
            contentDescription = stringResource(R.string.cd_comment),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = comments, color = MaterialTheme.colorScheme.onSurface)
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
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column {
            PostHeader(post)
            PostImage(post.imageRes)

            Text(
                text = stringResource(R.string.post_lorem),
                modifier = Modifier.padding(20.dp),
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp
            )

            PostActions(post.likes, post.comments)
        }
    }
}

@Composable
fun BottomItem(icon: Int, labelRes: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(labelRes),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = stringResource(labelRes),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun BottomFloatingBar(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        shape = RoundedCornerShape(60.dp),
        elevation = CardDefaults.cardElevation(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 22.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomItem(R.drawable.ic_explore, R.string.nav_explore)
            BottomItem(R.drawable.ic_home, R.string.nav_home)
            BottomItem(R.drawable.ic_profile, R.string.nav_profile)
        }
    }
}

@Composable
fun HomeScreenRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNotifications: () -> Unit = {}
) {
    val initialPosts = remember { PostRepository.getPosts() }

    var state by remember {
        mutableStateOf(HomeUiState(posts = initialPosts))
    }

    HomeScreenContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onNotifications = onNotifications,
        onPostClick = { index ->
            state = state.copy(selectedPostIndex = index)
        }
    )
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNotifications: () -> Unit,
    onPostClick: (Int) -> Unit
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { HomeTopBar(onBack = onBack, onNotifications = onNotifications) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(bottom = 22.dp)
            ) {
                BottomFloatingBar(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 30.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection()

            Spacer(Modifier.height(20.dp))

            state.posts.forEachIndexed { index, post ->
                PostCard(
                    post = post,
                    isSelected = state.selectedPostIndex == index,
                    onClick = { onPostClick(index) }
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    CondorappTheme(darkTheme = false) {
        HomeScreenRoute()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenDarkPreview() {
    CondorappTheme(darkTheme = true) {
        HomeScreenRoute()
    }
}