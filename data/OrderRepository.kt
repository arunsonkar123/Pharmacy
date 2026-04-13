package com.example.pharmacyapp.data

import com.example.pharmacyapp.data.model.CartItem
import com.example.pharmacyapp.data.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OrderRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun placeOrder(
        customerName: String,
        phoneNumber: String,
        address: String,
        paymentMethod: String,
        items: List<CartItem>,
        onResult: (Boolean, String) -> Unit
    ) {
        if (customerName.isBlank() || phoneNumber.isBlank() || address.isBlank()) {
            onResult(false, "Please fill all fields")
            return
        }

        if (items.isEmpty()) {
            onResult(false, "Cart is empty")
            return
        }

        val orderId = db.collection("orders").document().id
        val userId = auth.currentUser?.uid ?: "guest_user"
        val totalAmount = items.sumOf { it.price * it.quantity }

        val order = OrderModel(
            orderId = orderId,
            userId = userId,
            customerName = customerName,
            phoneNumber = phoneNumber,
            address = address,
            items = items,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            status = "Pending"
        )

        db.collection("orders")
            .document(orderId)
            .set(order)
            .addOnSuccessListener {
                onResult(true, "Order placed successfully")
            }
            .addOnFailureListener { e ->
                onResult(false, e.message ?: "Failed to place order")
            }
    }

    fun getOrdersOfCurrentUser(
        onResult: (List<OrderModel>) -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: "guest_user"

        db.collection("orders")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val orders = result.documents.mapNotNull { document ->
                    document.toObject(OrderModel::class.java)
                }.sortedByDescending { it.orderId }
                onResult(orders)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to load orders")
            }
    }
}