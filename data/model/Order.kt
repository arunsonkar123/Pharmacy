package com.example.pharmacyapp.data.model

import com.google.firebase.Timestamp

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val customerName: String = "",
    val phone: String = "",
    val address: String = "",
    val totalAmount: Double = 0.0,
    val paymentMethod: String = "",
    val status: String = "Placed",
    val createdAt: Timestamp? = null,
    val items: List<OrderItem> = emptyList()
)