package com.example.condorapp.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.condorapp.R

@Composable
fun SplashScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToInicio: () -> Unit
) {
    val splashState by viewModel.splashState.collectAsStateWithLifecycle()

    LaunchedEffect(splashState) {
        when (splashState) {
            is SplashState.Authenticated -> onNavigateToHome()
            is SplashState.Unauthenticated -> onNavigateToInicio()
            is SplashState.Loading -> { /* Keep showing splash */ }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
    }
}
