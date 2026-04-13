package com.example.pharmacyapp.data

data class OrderModel(
    val orderId: String = "",
    val userId: String = "",
    val customerName: String = "",
    val phone: String = "",
    val address: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "",
    val status: String = "",
    val timestamp: Long = 0L
)