@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R

private val LightBackground = Color(0xFFF5F7F1)
private val Green = Color(0xFF4F7336)
private val LightGreen = Color(0xFFA1BE8F)
private val Gray = Color(0xFF7A7A7A)
private val White = Color.White
private val Black = Color.Black

data class Post(
    val user: String,
    val location: String,
    val imageRes: Int,
    val likes: String,
    val comments: String
)

@Composable
fun HomeTopBar() {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Green
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = Green
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LightBackground
        )
    )
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar()
}
@Composable
fun ProfileAvatar() {
    Image(
        painter = painterResource(R.drawable.avatar),
        contentDescription = null,
        modifier = Modifier
            .size(90.dp)
            .clip(CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileAvatarPreview() {
    ProfileAvatar()
}

@Composable
fun FilterBar() {
    Row(
        modifier = Modifier
            .background(LightGreen, RoundedCornerShape(40.dp))
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = null,
            tint = Green,
            modifier = Modifier
                .size(45.dp)
                .padding(start = 10.dp)
        )

        Spacer(Modifier.width(40.dp))

        Text(
            "Filtro",
            color = Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(Modifier.width(40.dp))

        Icon(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = null,
            tint = Green,
            modifier = Modifier
                .size(45.dp)
                .padding(end = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterBarPreview() {
    FilterBar()
}

@Composable
fun HeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        ProfileAvatar()
        Spacer(Modifier.height(12.dp))
        FilterBar()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderSectionPreview() {
    HeaderSection()
}

@Composable
fun PostHeader(post: Post) {
    Row(
        modifier = Modifier.padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = post.user.first().toString(),
            fontWeight = FontWeight.Bold,
            color = Green,
            fontSize = 20.sp
        )

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(post.user, fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Text(post.location, color = Gray, fontSize = 14.sp)
        }

        Icon(
            painter = painterResource(R.drawable.ic_more),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PostHeaderPreview() {
    PostHeader(
        Post("Alejandra Gomez","Valle del Cocora",R.drawable.cartagena,"1.2k","58")
    )
}

@Composable
fun PostImage(imageRes: Int) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PostImagePreview() {
    PostImage(R.drawable.cartagena)
}

@Composable
fun PostActions(likes: String, comments: String) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_heart),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(likes)

        Spacer(Modifier.width(30.dp))

        Icon(
            painter = painterResource(R.drawable.ic_comment),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(comments)
    }
}

@Preview(showBackground = true)
@Composable
fun PostActionsPreview() {
    PostActions("1.2k","58")
}

@Composable
fun PostCard(post: Post) {
    Card(
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column {

            PostHeader(post)

            PostImage(post.imageRes)

            Text(
                "Lorem ipsum dolor sit amet...",
                modifier = Modifier.padding(20.dp),
                color = Gray,
                fontSize = 14.sp
            )

            PostActions(post.likes, post.comments)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostCardPreview() {
    PostCard(
        Post("Alejandra Gomez","Valle del Cocora",R.drawable.cartagena,"1.2k","58")
    )
}

@Composable
fun BottomItem(icon: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )
        Spacer(Modifier.height(6.dp))
        Text(label, fontSize = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomItemPreview() {
    BottomItem(R.drawable.ic_home,"Home")
}

@Composable
fun BottomFloatingBar(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        shape = RoundedCornerShape(60.dp),
        elevation = CardDefaults.cardElevation(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(White)
                .padding(vertical = 22.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomItem(R.drawable.ic_explore, "Explore")
            BottomItem(R.drawable.ic_home, "Home")
            BottomItem(R.drawable.ic_profile, "Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomFloatingBarPreview() {
    BottomFloatingBar()
}

@Composable
fun HomeScreen() {

    val posts = listOf(
        Post("Alejandra Gomez","Valle del Cocora",R.drawable.valle_del_cocora,"1.2k","58"),
        Post("Mateo Ruiz","Cartagena Old City",R.drawable.cartagena,"980","30")
    )

    Scaffold(
        containerColor = LightBackground,
        topBar = { HomeTopBar() }
    ) { innerPadding ->

        Box(Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {

                HeaderSection()

                Spacer(Modifier.height(20.dp))

                posts.forEach { PostCard(it) }

                Spacer(Modifier.height(160.dp))
            }

            BottomFloatingBar(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 22.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

