package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.impl.data.CartRepository

internal class RemoveProductUseCase(
    private val repository: CartRepository,
) {
    suspend operator fun invoke(productId: Long) {
        repository.removeProduct(productId)
    }
}
