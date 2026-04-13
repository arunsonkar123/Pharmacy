package com.example.pharmacyapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AdminOrderRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getAllOrders(): Result<List<OrderModel>> {
        return try {
            val snapshot = firestore.collection("orders").get().await()

            val orders = snapshot.documents.map { document ->
                @Suppress("UNCHECKED_CAST")
                val itemsData = document.get("items") as? List<Map<String, Any>> ?: emptyList()

                val items = itemsData.map { itemMap ->
                    CartItem(
                        id = itemMap["id"]?.toString() ?: "",
                        name = itemMap["name"]?.toString() ?: "",
                        price = (itemMap["price"] as? Number)?.toInt() ?: 0,
                        quantity = (itemMap["quantity"] as? Number)?.toInt() ?: 0
                    )
                }

                OrderModel(
                    orderId = document.getString("orderId") ?: "",
                    userId = document.getString("userId") ?: "",
                    customerName = document.getString("customerName") ?: "",
                    phone = document.getString("phone") ?: "",
                    address = document.getString("address") ?: "",
                    items = items,
                    totalAmount = (document.get("totalAmount") as? Number)?.toDouble() ?: 0.0,
                    paymentMethod = document.getString("paymentMethod") ?: "",
                    status = document.getString("status") ?: "",
                    timestamp = (document.get("timestamp") as? Number)?.toLong() ?: 0L
                )
            }.sortedByDescending { it.timestamp }

            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Unit> {
        return try {
            val snapshot = firestore.collection("orders")
                .whereEqualTo("orderId", orderId)
                .get()
                .await()

            if (snapshot.documents.isNotEmpty()) {
                val docId = snapshot.documents.first().id
                firestore.collection("orders")
                    .document(docId)
                    .update("status", newStatus)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}