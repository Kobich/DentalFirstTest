package com.dentalfirst.feature.cart.impl.data.local

import kotlinx.coroutines.flow.Flow

internal interface CartStorage {
    fun observeQuantities(): Flow<Map<Long, Int>>

    suspend fun updateQuantities(transform: (MutableMap<Long, Int>) -> Unit)
}
