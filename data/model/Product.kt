package com.example.pharmacyapp.data.model

data class Product(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val price: Int = 0,
    val stock: Int = 0
)