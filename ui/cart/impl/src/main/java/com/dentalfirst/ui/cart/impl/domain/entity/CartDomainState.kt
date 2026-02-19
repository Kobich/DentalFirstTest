package com.dentalfirst.ui.cart.impl.domain.entity

import com.dentalfirst.feature.cart.api.CartState

internal sealed interface CartDomainState {
    data object Loading : CartDomainState
    data object Empty : CartDomainState
    data class Content(val cartState: CartState) : CartDomainState
}
