package com.example.condorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.condorapp.ui.*
import com.example.condorapp.ui.theme.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CondorappTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Definimos en qué pantallas queremos mostrar la barra de navegación
                val showBottomBar = currentRoute in listOf(
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
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Inicio.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. INICIO
                        composable(Screen.Inicio.route) {
                            InicioScreenRoute(
                                onStartClick = { navController.navigate(Screen.Login.route) }
                            )
                        }

                        // 2. LOGIN
                        composable(Screen.Login.route) {
                            LoginScreenRoute(
                                onSignInSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                },
                                onGoRegister = { navController.navigate(Screen.SignUp.route) }
                            )
                        }

                        // 3. SIGN UP
                        composable(Screen.SignUp.route) {
                            SignUpScreenRoute(
                                onRegisterSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.SignUp.route) { inclusive = true }
                                    }
                                },
                                onCancel = { navController.popBackStack() }
                            )
                        }

                        // 4. HOME
                        composable(Screen.Home.route) {
                            HomeScreenRoute(
                                onPostClick = { postId ->
                                    navController.navigate(Screen.Details.createRoute(postId))
                                },
                                onNotificationsClick = { navController.navigate(Screen.Notifications.route) }
                            )
                        }

                        // 5. EXPLORAR
                        composable(Screen.Explore.route) {
                            FeedScreenRoute(
                                onPlaceClick = { placeId ->
                                    navController.navigate(Screen.Details.createRoute(placeId))
                                }
                            )
                        }

                        // 6. PERFIL
                        composable(Screen.Profile.route) {
                            ProfileScreenRoute(
                                onBack = { navController.popBackStack() },
                                onEditProfile = { navController.navigate(Screen.EditProfile.route) }
                            )
                        }

                        // 7. NOTIFICACIONES
                        composable(Screen.Notifications.route) {
                            NotificationsScreenRoute(
                                onBack = { navController.popBackStack() }
                            )
                        }

                        // 8. RESEÑA
                        composable(Screen.Review.route) {
                            ReviewScreenRoute(
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        // 9. DETALLES
                        composable(Screen.Details.route) { backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId") ?: ""
                            DetailScreen(
                                postId = postId,
                                onBack = { navController.popBackStack() },
                                onAddReview = { navController.navigate(Screen.Review.route) }
                            )
                        }

                        // 10. EDITAR PERFIL
                        composable(Screen.EditProfile.route) {
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

@Composable
fun BottomFloatingBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .height(80.dp),
            shape = RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationItem(
                    iconRes = R.drawable.ic_home,
                    label = "Home",
                    route = Screen.Home.route,
                    isSelected = currentRoute == Screen.Home.route,
                    onClick = { onNavigate(Screen.Home.route) }
                )
                NavigationItem(
                    iconRes = R.drawable.ic_explore,
                    label = "Explore",
                    route = Screen.Explore.route,
                    isSelected = currentRoute == Screen.Explore.route,
                    onClick = { onNavigate(Screen.Explore.route) }
                )
                NavigationItem(
                    iconRes = R.drawable.ic_profile,
                    label = "Profile",
                    route = Screen.Profile.route,
                    isSelected = currentRoute == Screen.Profile.route,
                    onClick = { onNavigate(Screen.Profile.route) }
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    iconRes: Int,
    label: String,
    route: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
