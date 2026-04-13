package com.example.pharmacyapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminProductRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addProduct(product: ProductModel): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(product.id)
                .set(product)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(product: ProductModel): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(product.id)
                .set(product)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(id)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}