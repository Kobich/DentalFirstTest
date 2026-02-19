package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.impl.data.CartRepository

internal class AddProductUseCase(
    private val repository: CartRepository,
) {
    suspend operator fun invoke(productId: Long, quantity: Int) {
        repository.addProduct(productId, quantity)
    }
}
