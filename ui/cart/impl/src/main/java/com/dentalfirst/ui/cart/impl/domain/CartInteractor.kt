package com.dentalfirst.ui.cart.impl.domain

import com.dentalfirst.feature.cart.api.CartFeature
import com.dentalfirst.ui.cart.impl.domain.entity.CartDomainState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

internal class CartInteractor(
    private val cartFeature: CartFeature,
) {
    fun observeState(): Flow<CartDomainState> {
        return cartFeature.observeCart()
            .map { cartState ->
                if (cartState.items.isEmpty()) {
                    CartDomainState.Empty
                } else {
                    CartDomainState.Content(cartState)
                }
            }
            .onStart { emit(CartDomainState.Loading) }
    }

    suspend fun increase(productId: Long) {
        cartFeature.changeQuantity(productId = productId, delta = 1)
    }

    suspend fun decrease(productId: Long) {
        cartFeature.changeQuantity(productId = productId, delta = -1)
    }

    suspend fun remove(productId: Long) {
        cartFeature.removeProduct(productId)
    }
}
