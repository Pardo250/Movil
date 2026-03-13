package com.example.condorapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.condorapp.ui.createreview.CreateReviewScreenRoute
import com.example.condorapp.ui.detail.DetailScreenRoute
import com.example.condorapp.ui.editprofile.EditProfileScreenRoute
import com.example.condorapp.ui.feed.FeedScreenRoute
import com.example.condorapp.ui.home.HomeScreenRoute
import com.example.condorapp.ui.inicio.InicioScreenRoute
import com.example.condorapp.ui.login.LoginScreenRoute
import com.example.condorapp.ui.notifications.NotificationsScreenRoute
import com.example.condorapp.ui.profile.ProfileScreenRoute
import com.example.condorapp.ui.review.ReviewScreenRoute
import com.example.condorapp.ui.signup.SignUpScreenRoute
import com.example.condorapp.ui.splash.SplashScreenRoute

/**
 * Navegación centralizada de la aplicación. Define todas las rutas y la lógica de navegación entre
 * pantallas.
 */
@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreenRoute(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToInicio = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 1. INICIO
        composable(Screen.Inicio.route) {
            InicioScreenRoute(onStartClick = { navController.navigate(Screen.Login.route) })
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
            NotificationsScreenRoute(onBack = { navController.popBackStack() })
        }

        // 8. RESEÑA (DETALLE)
        composable(Screen.Review.route) { backStackEntry ->
            val reviewId = backStackEntry.arguments?.getString("reviewId") ?: "1"
            ReviewScreenRoute(reviewId = reviewId, onBackClick = { navController.popBackStack() })
        }

        // 9. DETALLES
        composable(Screen.Details.route) { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            DetailScreenRoute(
                    postId = postId,
                    onBack = { navController.popBackStack() },
                    onAddReview = { navController.navigate(Screen.CreateReview.route) },
                    onReviewClick = { reviewId ->
                        navController.navigate(Screen.Review.createRoute(reviewId))
                    }
            )
        }

        // 10. EDITAR PERFIL
        composable(Screen.EditProfile.route) {
            EditProfileScreenRoute(
                onBack = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 11. CREAR RESEÑA
        composable(Screen.CreateReview.route) {
            CreateReviewScreenRoute(onBackClick = { navController.popBackStack() })
        }
    }
}
