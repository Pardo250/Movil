package com.example.condorapp.ui

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object Explore : Screen("explore")
    object Profile : Screen("profile")
    object Notifications : Screen("notifications")
    object Review : Screen("review")
    object EditProfile : Screen("edit_profile")
    object Details : Screen("details/{postId}") {
        fun createRoute(postId: String) = "details/$postId"
    }
}
