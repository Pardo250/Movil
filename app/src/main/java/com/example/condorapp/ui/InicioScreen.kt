package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.condorapp.ui.theme.CondorappTheme

data class InicioUiState(
    val slogan: String = "EXPLORA EL REALISMO MÁGICO"
)

@Composable
fun InicioScreenRoute(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit   // ✅ callback para navegar
) {
    val state by remember { mutableStateOf(InicioUiState()) }

    InicioScreenContent(
        state = state,
        onStartClick = onStartClick,
        modifier = modifier
    )
}

@Composable
fun InicioScreenContent(
    state: InicioUiState,
    onStartClick: () -> Unit,   // ✅ se pasa al contenido
    modifier: Modifier = Modifier
) {

    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            LogoApp()

            Spacer(modifier = Modifier.height(32.dp))

            Slogan(text = state.slogan)

            Spacer(modifier = Modifier.height(32.dp))

            // ✅ BOTÓN PARA IR AL LOGIN
            Button(
                onClick = onStartClick
            ) {
                Text("Comenzar")
            }
        }
    }
}

@Composable
fun LogoApp(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = stringResource(R.string.cd_logo),
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
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        fontSize = 22.sp,
        fontWeight = FontWeight.Light,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun InicioScreenLightPreview() {
    CondorappTheme(darkTheme = false) {
        InicioScreenRoute(
            onStartClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InicioScreenDarkPreview() {
    CondorappTheme(darkTheme = true) {
        InicioScreenRoute(
            onStartClick = {}
        )
    }
}