package com.dentalfirst.feature.cart.impl.data

import com.dentalfirst.feature.cart.api.ProductQuantity
import com.dentalfirst.feature.cart.impl.data.local.CartStorage
import kotlinx.coroutines.flow.Flow

internal class CartRepositoryImpl(
    private val storage: CartStorage,
) : CartRepository {

    override fun observeQuantities(): Flow<Map<Long, Int>> = storage.observeQuantities()

    override suspend fun addProduct(productId: Long, quantity: Int) {
        if (quantity <= 0) return

        storage.updateQuantities { quantities ->
            val current = quantities[productId] ?: 0
            quantities[productId] = current + quantity
        }
    }

    override suspend fun addProducts(products: List<ProductQuantity>) {
        if (products.isEmpty()) return

        storage.updateQuantities { quantities ->
            products.forEach { item ->
                if (item.quantity <= 0) return@forEach

                val current = quantities[item.productId] ?: 0
                quantities[item.productId] = current + item.quantity
            }
        }
    }

    override suspend fun changeQuantity(productId: Long, delta: Int) {
        if (delta == 0) return

        storage.updateQuantities { quantities ->
            val current = quantities[productId] ?: 0
            val updated = current + delta

            if (updated <= 0) {
                quantities.remove(productId)
            } else {
                quantities[productId] = updated
            }
        }
    }

    override suspend fun removeProduct(productId: Long) {
        storage.updateQuantities { quantities ->
            quantities.remove(productId)
        }
    }
}
