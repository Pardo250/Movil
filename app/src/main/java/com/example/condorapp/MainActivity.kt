package com.example.condorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.condorapp.ui.theme.CondorappTheme

import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de CondorApp. Solo se encarga de la inicialización de la app y aplicar el
 * tema.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent { CondorappTheme { CondorApp() } }
    }
}
