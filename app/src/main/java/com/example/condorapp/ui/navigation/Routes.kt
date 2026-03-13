package com.example.condorapp.ui.navigation

/**
 * Sealed class que define todas las rutas de navegación de la aplicación. Centraliza la definición
 * de pantallas para evitar errores con strings.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Inicio : Screen("inicio")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")
    object Review : Screen("review/{reviewId}") {
        fun createRoute(reviewId: String) = "review/$reviewId"
    }
    object CreateReview : Screen("create_review")
    object EditProfile : Screen("edit_profile")
    object Details : Screen("details/{postId}") {
        fun createRoute(postId: String) = "details/$postId"
    }
}
