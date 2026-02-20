package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.condorapp.R

data class InicioUiState(
    val slogan: String = "EXPLORA EL REALISMO MAGICO"
)

@Composable
fun LogoApp(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "logo",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(180.dp)
            .clip(CircleShape)
    )
}

@Composable
fun Slogan(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = colorResource(id = R.color.Grisclaro),
        modifier = modifier
    )
}

@Composable
fun InicioScreenRoute(
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf(InicioUiState()) }

    InicioScreenContent(
        state = state,
        modifier = modifier
    )
}

@Composable
fun InicioScreenContent(
    state: InicioUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.Olivaoscuro))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            LogoApp()
            Spacer(modifier = Modifier.size(32.dp))
            Slogan(text = state.slogan)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenPreview() {
    InicioScreenRoute()
}

annotation class InicioScreen
