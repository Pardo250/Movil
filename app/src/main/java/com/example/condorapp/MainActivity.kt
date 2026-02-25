package com.example.condorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.condorapp.ui.HomeScreenRoute
import com.example.condorapp.ui.theme.CondorappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CondorappTheme {
                var currentScreen by remember { mutableStateOf("login") }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    val screenModifier = Modifier.padding(innerPadding)

                    when (currentScreen) {
                        "login" -> HomeScreenRoute(
                            modifier = screenModifier,
                        )
                    }
                }
            }
        }
    }
}
