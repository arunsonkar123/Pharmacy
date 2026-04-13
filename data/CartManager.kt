package com.example.pharmacyapp.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CartItem(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val quantity: Int = 1
)

data class OrderItem(
    val id: String = "",
    val items: List<CartItem> = emptyList(),
    val total: Int = 0,
    val paymentMethod: String = "",
    val address: String = ""
)

object CartManager {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderItem>>(emptyList())
    val orders: StateFlow<List<OrderItem>> = _orders.asStateFlow()

    fun addItem(id: String, name: String, price: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }

        if (index != -1) {
            val item = current[index]
            current[index] = item.copy(quantity = item.quantity + 1)
        } else {
            current.add(CartItem(id = id, name = name, price = price, quantity = 1))
        }

        _cartItems.value = current.toList()
    }

    fun increaseItem(name: String) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.name == name }

        if (index != -1) {
            val item = current[index]
            current[index] = item.copy(quantity = item.quantity + 1)
            _cartItems.value = current.toList()
        }
    }

    fun decreaseItem(name: String) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.name == name }

        if (index != -1) {
            val item = current[index]
            if (item.quantity > 1) {
                current[index] = item.copy(quantity = item.quantity - 1)
            } else {
                current.removeAt(index)
            }
            _cartItems.value = current.toList()
        }
    }

    fun getTotalPrice(): Int {
        return _cartItems.value.sumOf { it.price * it.quantity }
    }

    fun placeOrder(address: String, paymentMethod: String) {
        val items = _cartItems.value
        if (items.isEmpty()) return

        val order = OrderItem(
            id = "ORD${System.currentTimeMillis()}",
            items = items,
            total = getTotalPrice(),
            paymentMethod = paymentMethod,
            address = address
        )

        _orders.value = listOf(order) + _orders.value
        _cartItems.value = emptyList()
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}