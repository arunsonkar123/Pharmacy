package com.example.pharmacyapp.data

class CheckoutRepository {

    fun reduceStock(cartItems: List<CartItem>): Result<Boolean> {
        return try {
            // Abhi simple success return kar rahe hain
            // Baad me isme Firebase stock update add karenge
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}