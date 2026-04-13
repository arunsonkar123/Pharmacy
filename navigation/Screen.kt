package com.example.pharmacyapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Orders : Screen("orders")
    object Profile : Screen("profile")
    object Checkout : Screen("checkout")
}