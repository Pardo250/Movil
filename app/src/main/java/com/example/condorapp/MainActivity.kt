package com.example.condorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.condorapp.ui.navigation.AppNavigation
import com.example.condorapp.ui.navigation.BottomFloatingBar
import com.example.condorapp.ui.navigation.Screen
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Activity principal de CondorApp. Contiene el único Scaffold de la aplicación con la barra de
 * navegación inferior y delega toda la navegación a AppNavigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CondorappTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Pantallas donde se muestra la barra de navegación inferior
                val showBottomBar =
                        currentRoute in
                                listOf(
                                        Screen.Home.route,
                                        Screen.Explore.route,
                                        Screen.Profile.route,
                                        Screen.Notifications.route
                                )

                Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            if (showBottomBar) {
                                BottomFloatingBar(
                                        currentRoute = currentRoute,
                                        onNavigate = { route ->
                                            if (currentRoute != route) {
                                                navController.navigate(route) {
                                                    popUpTo(Screen.Home.route) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        }
                                )
                            }
                        }
                ) { innerPadding ->
                    AppNavigation(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
