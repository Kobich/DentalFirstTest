package com.dentalfirst.feature.cart.impl.data

import com.dentalfirst.feature.cart.api.ProductQuantity
import kotlinx.coroutines.flow.Flow

internal interface CartRepository {
    fun observeQuantities(): Flow<Map<Long, Int>>

    suspend fun addProduct(productId: Long, quantity: Int)

    suspend fun addProducts(products: List<ProductQuantity>)

    suspend fun changeQuantity(productId: Long, delta: Int)

    suspend fun removeProduct(productId: Long)
}
