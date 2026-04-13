package com.example.pharmacyapp.data.model

data class CartItem(
    val productId: String = "",
    val name: String = "",
    val price: Int = 0,
    val quantity: Int = 1
)