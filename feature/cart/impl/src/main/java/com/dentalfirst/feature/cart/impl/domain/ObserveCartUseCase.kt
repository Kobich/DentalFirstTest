package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.impl.data.CartRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveCartUseCase(
    private val repository: CartRepository,
) {
    operator fun invoke(): Flow<Map<Long, Int>> = repository.observeQuantities()
}
