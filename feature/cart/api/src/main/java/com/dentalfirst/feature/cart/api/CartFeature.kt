package com.dentalfirst.feature.cart.api

import kotlinx.coroutines.flow.Flow

interface CartFeature {
    fun observeCart(): Flow<CartState>

    suspend fun getAvailableProducts(): List<Product>

    suspend fun addProduct(productId: Long, quantity: Int = 1)

    suspend fun addProducts(products: List<ProductQuantity>)

    suspend fun changeQuantity(productId: Long, delta: Int)

    suspend fun removeProduct(productId: Long)

    suspend fun fillWithDemoProducts(count: Int = 3)
}
