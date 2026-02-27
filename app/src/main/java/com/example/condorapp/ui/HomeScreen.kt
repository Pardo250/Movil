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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.condorapp.R
import com.example.condorapp.data.Post
import com.example.condorapp.data.local.PostRepository
import com.example.condorapp.ui.theme.CondorappTheme

// --- CONFIGURACIÓN DE COLORES DEL PROYECTO ---
val DesignGreenLight = Color(0xFFE2E3D8)
val DesignGreenDark = Color(0xFF43664B)
val DesignSelectedPill = Color(0xFFD3E6D4)
val BackgroundApp = Color(0xFFF8F9F5)

// Definición de rutas para la navegación
object Destinos {
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val PROFILE = "profile"
    const val DETAILS = "details"
    const val EDIT_PROFILE = "edit_profile"
}

data class HomeUiState(
    val posts: List<Post> = emptyList(),
    val selectedPostIndex: Int? = null
)

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNotifications: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = Color.Black
                )
            }
        },
        actions = {
            IconButton(onClick = onNotifications) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = stringResource(R.string.cd_notifications),
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(R.drawable.avatar),
        contentDescription = stringResource(R.string.cd_profile_photo),
        modifier = modifier
            .size(90.dp)
            .clip(CircleShape)
    )
}

@Composable
fun FilterBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .width(280.dp)
            .background(
                color = DesignGreenLight,
                shape = RoundedCornerShape(40.dp)
            )
            .padding(vertical = 12.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = stringResource(R.string.cd_menu),
            tint = DesignGreenDark,
            modifier = Modifier.size(36.dp)
        )

        Text(
            text = stringResource(R.string.filter),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = stringResource(R.string.cd_logo),
            tint = DesignGreenDark,
            modifier = Modifier.size(38.dp)
        )
    }
}

@Composable
fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileAvatar()
        Spacer(Modifier.height(16.dp))
        FilterBar()
    }
}

@Composable
fun PostHeader(
    post: Post
) {
    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = post.user.first().toString(),
            fontWeight = FontWeight.Bold,
            color = DesignGreenDark,
            fontSize = 22.sp,
            modifier = Modifier.width(30.dp)
        )

        Spacer(Modifier.width(16.dp))

        Column(
            Modifier.weight(1f)
        ) {
            Text(
                text = post.user,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = post.location,
                color = Color.Gray,
                fontSize = 20.sp
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = stringResource(R.string.cd_more_options),
            tint = Color.Black,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun PostImage(
    imageRes: Int
) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = stringResource(R.string.cd_post_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    )
}

@Composable
fun PostActions(
    likes: String,
    comments: String
) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_heart),
            contentDescription = stringResource(R.string.cd_like),
            tint = Color.Black,
            modifier = Modifier.size(38.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = likes, color = Color.Black)

        Spacer(Modifier.width(32.dp))

        Icon(
            painter = painterResource(R.drawable.ic_comment),
            contentDescription = stringResource(R.string.cd_comment),
            tint = Color.Black,
            modifier = Modifier.size(38.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text = comments, color = Color.Black)
    }
}

@Composable
fun PostCard(
    post: Post,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF1F4F1) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 2.dp else 10.dp)
    ) {
        Column {
            PostHeader(post)
            PostImage(post.imageRes)
            Text(
                text = stringResource(R.string.post_lorem),
                modifier = Modifier.padding(32.dp),
                color = Color.DarkGray,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
            PostActions(post.likes, post.comments)
        }
    }
}

@Composable
fun NavigationItem(
    iconRes: Int,
    label: String,
    route: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isSelected = navBackStackEntry?.destination?.route == route

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .width(110.dp)
            .clickable {
                if (!isSelected) navController.navigate(route)
            }
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)
                .height(50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(if (isSelected) DesignSelectedPill else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = if (isSelected) DesignGreenDark else Color.DarkGray,
                modifier = Modifier.size(50.dp) // ICONOS GRANDES
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun BottomFloatingBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .height(110.dp),
        shape = RoundedCornerShape(60.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem(R.drawable.ic_explore, "Explore", Destinos.EXPLORE, navController)
            NavigationItem(R.drawable.ic_home, "Home", Destinos.HOME, navController)
            NavigationItem(R.drawable.ic_profile, "Profile", Destinos.PROFILE, navController)
        }
    }
}

@Composable
fun HomeScreenRoute(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onNotifications: () -> Unit = {}
) {
    val posts = remember { PostRepository.getPosts() }
    var state by remember { mutableStateOf(HomeUiState(posts = posts)) }

    HomeScreenContent(
        state = state,
        modifier = modifier,
        navController = navController,
        onBack = onBack,
        onNotifications = onNotifications,
        onPostClick = { index ->
            state = state.copy(selectedPostIndex = index)
            // AL OPRIMIR LA TARJETA VA AL DETALLE
            val post = state.posts[index]
            navController.navigate("${Destinos.DETAILS}/${post.location}")
        }
    )
}

@Composable
fun HomeScreenContent(
    state: HomeUiState,
    modifier: Modifier,
    navController: NavHostController,
    onBack: () -> Unit,
    onNotifications: () -> Unit,
    onPostClick: (Int) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundApp)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 150.dp)
        ) {
            item {
                HomeTopBar(onBack = onBack, onNotifications = onNotifications)
            }
            item {
                HeaderSection()
                Spacer(Modifier.height(20.dp))
            }
            itemsIndexed(state.posts) { index, post ->
                PostCard(
                    post = post,
                    isSelected = state.selectedPostIndex == index,
                    onClick = { onPostClick(index) }
                )
            }
        }

        BottomFloatingBar(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}