package com.example.condorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.condorapp.ui.HomeScreenRoute
import com.example.condorapp.ui.LoginScreenRoute
import com.example.condorapp.ui.InicioScreenRoute
import com.example.condorapp.ui.DetailScreen
import com.example.condorapp.ui.FeedScreenRoute
import com.example.condorapp.ui.ProfileScreenRoute
import com.example.condorapp.ui.EditProfileScreenRoute
import com.example.condorapp.ui.Destinos
import com.example.condorapp.ui.theme.CondorappTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CondorappTheme {
                // Controlador maestro de la navegación
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->


                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. SPLASH / INICIO
                        composable("inicio") {
                            InicioScreenRoute(
                                onStartClick = { navController.navigate("login") }
                            )
                        }

                        // 2. LOGIN
                        composable("login") {
                            LoginScreenRoute(
                                onSignInSuccess = {
                                    navController.navigate(Destinos.HOME) {
                                        // Limpiamos el stack para que no regrese al login al dar atrás
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 3. HOME (FEED DE POSTS )
                        composable(Destinos.HOME) {
                            HomeScreenRoute(
                                navController = navController,
                                onBack = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 4. EXPLORAR (FEED)
                        composable(Destinos.EXPLORE) {
                            FeedScreenRoute(
                                navController = navController
                            )
                        }

                        // 5. PERFIL
                        composable(Destinos.PROFILE) {
                            ProfileScreenRoute(
                                navController = navController,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // 6. DETALLES
                        composable("${Destinos.DETAILS}/{postId}") { backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId") ?: ""

                            DetailScreen(
                                postId = postId,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // 7. EDITAR PERFIL
                        composable(Destinos.EDIT_PROFILE) {
                            EditProfileScreenRoute(
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}