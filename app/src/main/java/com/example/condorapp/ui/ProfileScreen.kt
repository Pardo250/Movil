package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R



@Composable
fun Fondofeed () {
    Box( modifier = Modifier .fillMaxSize() .background(colorResource(id = R.color.Olivafeed))
    ) {}
}

@Composable
@Preview (showBackground = true)
fun FondofeedPreview() {
    Fondofeed()
}
@Composable
fun ProfileScreen() {

    Box(modifier = Modifier.fillMaxSize()) {

        Fondofeed()

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(50.dp))

            ProfileTopBar()

            Spacer(modifier = Modifier.height(20.dp))

            ProfileHeader()

            Spacer(modifier = Modifier.height(20.dp))

            ProfileActions()

            Spacer(modifier = Modifier.height(20.dp))

            ProfileTabs()

            Spacer(modifier = Modifier.height(20.dp))

            PhotoGrid(modifier = Modifier.weight(1f))
        }

        BottomNavBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 40.dp, vertical = 20.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Composable
fun ProfileTopBar() {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileTopBarPreview() {
    ProfileTopBar()
}

@Composable
fun ProfileHeader() {

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {

        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Camilo Jiménez",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "@camilojim_co",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader()
}

@Composable
fun ProfileActions() {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        Button(
            onClick = { },
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Editar Perfil")
        }

        Button(
            onClick = { },
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("Compartir Perfil")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileActionsPreview() {
    ProfileActions()
}

@Composable
fun ProfileTabs() {

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Text("Reseñas")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.ChatBubble, contentDescription = null)
            Text("Comentarios")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Bookmark, contentDescription = null)
            Text("Interés")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileTabsPreview() {
    ProfileTabs()
}

@Composable
fun PhotoGrid(modifier: Modifier = Modifier) {

    val photos = listOf(
        R.drawable.cartagena,
        R.drawable.valle_del_cocora,
        R.drawable.medellin,
        R.drawable.santamarta,
        R.drawable.catedral,
        R.drawable.atardecer
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(photos) { photo ->
            PhotoItem(photo)
        }
    }
}

@Composable
fun PhotoItem(imageRes: Int) {

    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun PhotoItemPreview() {
    PhotoItem(R.drawable.cartagena)
}

