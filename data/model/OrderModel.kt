package com.example.pharmacyapp.data.model

data class OrderModel(
    val orderId: String = "",
    val userId: String = "",
    val customerName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Int = 0,
    val paymentMethod: String = "",
    val status: String = "Pending"
)