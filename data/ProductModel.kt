package com.example.pharmacyapp.data

data class ProductModel(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val stock: Int = 0,
    val category: String = "",
    val imageUrl: String = ""
)