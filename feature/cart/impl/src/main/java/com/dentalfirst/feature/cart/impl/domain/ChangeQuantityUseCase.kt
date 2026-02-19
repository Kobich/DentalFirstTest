package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.impl.data.CartRepository

internal class ChangeQuantityUseCase(
    private val repository: CartRepository,
) {
    suspend operator fun invoke(productId: Long, delta: Int) {
        repository.changeQuantity(productId, delta)
    }
}
