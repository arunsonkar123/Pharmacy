package com.example.pharmacyapp.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getProducts(): Result<List<ProductModel>> {
        return try {
            val snapshot = firestore.collection("products").get().await()

            val products = snapshot.documents.mapNotNull { document ->
                val id = document.getString("id")?.trim().orEmpty()
                val name = document.getString("name")?.trim().orEmpty()
                val category = document.getString("category")?.trim().orEmpty()
                val imageUrl = document.getString("imageUrl")?.trim().orEmpty()
                val price = (document.get("price") as? Number)?.toInt() ?: -1
                val stock = (document.get("stock") as? Number)?.toInt() ?: -1

                if (
                    id.isBlank() ||
                    name.isBlank() ||
                    category.isBlank() ||
                    price < 0 ||
                    stock < 0
                ) {
                    null
                } else {
                    ProductModel(
                        id = id,
                        name = name,
                        price = price,
                        stock = stock,
                        category = category,
                        imageUrl = imageUrl
                    )
                }
            }.sortedBy { it.id }

            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}