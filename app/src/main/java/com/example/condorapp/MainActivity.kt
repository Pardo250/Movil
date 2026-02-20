package com.example.condorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.condorapp.ui.EditProfileScreenRoute
import com.example.condorapp.ui.FeedScreenRoute
import com.example.condorapp.ui.HomeScreenRoute
import com.example.condorapp.ui.InicioScreen
import com.example.condorapp.ui.InicioScreenRoute
import com.example.condorapp.ui.LoginScreenRoute


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                LoginScreenRoute()
            }

        }
        }
    }
