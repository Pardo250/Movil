package com.example.condorapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.condorapp.R
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip

val ScreenBg = Color(0xFFD9DBD3)
val DarkGreen = Color(0xFF2F4B2F)
val LightGreen = Color(0xFFA8B89E)
val VeryLightGreen = Color(0xFFC9D3C0)
val AvatarPink = Color(0xFFE96C7B)
val CardWhite = Color(0xFFF2F2F2)
val TextGray = Color(0xFF6B6B6B)

@Composable
fun NotificationsScreen(onBack: () -> Unit) {

    var selectedTab by remember { mutableStateOf("Todo") }

    val notifications = List(7) { "Maria Valen" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Surface(
                shape = CircleShape,
                color = VeryLightGreen,
                modifier = Modifier.size(40.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = DarkGreen
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Notificaciones",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Limpiar todo",
                color = LightGreen,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .background(
                    color = VeryLightGreen,
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(4.dp)
        ) {

            val tabs = listOf("Todo", "Menciones", "Followers", "likes")

            tabs.forEach { tab ->

                val isSelected = tab == selectedTab

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = if (isSelected) DarkGreen else Color.Transparent,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable { selectedTab = tab }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color.White else DarkGreen,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(notifications) {

                NotificationItem()
            }
        }
    }
}

@Composable
fun NotificationItem() {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    color = CardWhite,
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = "Maria Valen",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Ahora te sigue",
                    fontSize = 14.sp,
                    color = TextGray
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Button(
                onClick = { },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkGreen),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "Seguir También",
                    fontSize = 12.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "hace 2 días.",
                fontSize = 12.sp,
                color = TextGray,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewNotifications() {
    NotificationsScreen(onBack = {})
}