package com.example.pharmacyapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pharmacyapp.data.model.CartItem
import com.example.pharmacyapp.data.model.OrderModel
import com.example.pharmacyapp.data.model.Product
import com.example.pharmacyapp.navigation.Screen
import com.example.pharmacyapp.ui.cart.CartScreen
import com.example.pharmacyapp.ui.checkout.CheckoutScreen
import com.example.pharmacyapp.ui.home.HomeScreen
import com.example.pharmacyapp.ui.main.MainContainerScreen
import com.example.pharmacyapp.ui.order.OrdersScreen
import com.example.pharmacyapp.ui.profile.ProfileScreen
import com.example.pharmacyapp.ui.theme.PharmacyAppTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pharmacyapp.ui.auth.AuthViewModel
import com.example.pharmacyapp.ui.auth.LoginScreen
import com.example.pharmacyapp.ui.auth.RegisterScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PharmacyAppTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                val authState by authViewModel.uiState.collectAsState()
                var products by remember {
                    mutableStateOf(
                        listOf(
                            Product("p1", "Paracetamol", "Tablets", 50, 20),
                            Product("p2", "Cough Syrup", "Syrups", 95, 15),
                            Product("p3", "Bandages", "Medical", 60, 25),
                            Product("p4", "Vitamin C", "Supplements", 150, 10)
                        )
                    )
                }

                var cartItems by remember { mutableStateOf(listOf<CartItem>()) }
                var orders by remember { mutableStateOf(listOf<OrderModel>()) }

                val isAdmin = true
                val userName = ""
                val userEmail = ""

                fun addToCart(product: Product) {
                    val existing = cartItems.find { it.productId == product.id }
                    cartItems = if (existing != null) {
                        cartItems.map {
                            if (it.productId == product.id) it.copy(quantity = it.quantity + 1) else it
                        }
                    } else {
                        cartItems + CartItem(
                            productId = product.id,
                            name = product.name,
                            price = product.price,
                            quantity = 1
                        )
                    }
                }

                fun increaseQty(item: CartItem) {
                    cartItems = cartItems.map {
                        if (it.productId == item.productId) it.copy(quantity = it.quantity + 1) else it
                    }
                }

                fun decreaseQty(item: CartItem) {
                    cartItems = cartItems.mapNotNull {
                        if (it.productId == item.productId) {
                            if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else null
                        } else it
                    }
                }

                fun clearCart() {
                    cartItems = emptyList()
                }

                MainContainerScreen(
                    navController = navController,
                    isAdmin = isAdmin
                ) { modifier ->

                    NavHost(
                        navController = navController,
                        startDestination = if (authViewModel.isUserLoggedIn()) Screen.Home.route else "login",
                        modifier = modifier
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                navController = navController,
                                products = products,
                                onAddToCart = { addToCart(it) }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                viewModel = authViewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onRegisterClick = {
                                    navController.navigate("register")
                                }
                            )
                        }

                        composable("register") {
                            RegisterScreen(
                                viewModel = authViewModel,
                                onRegisterSuccess = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onLoginClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(Screen.Cart.route) {
                            CartScreen(
                                navController = navController,
                                cartItems = cartItems,
                                onIncrease = { increaseQty(it) },
                                onDecrease = { decreaseQty(it) }
                            )
                        }

                        composable(Screen.Checkout.route) {
                            CheckoutScreen(
                                cartItems = cartItems,
                                clearCart = { clearCart() },
                                onOrderPlaced = {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Order placed successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(Screen.Orders.route)
                                }
                            )
                        }

                        composable(Screen.Orders.route) {
                            OrdersScreen()
                        }

                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                userName = authState.name,
                                userEmail = authState.email,
                                onLogout = {
                                    authViewModel.logout()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("Orders") {
                            OrdersScreen()
                        }
                    }
                }
            }
        }
    }
}