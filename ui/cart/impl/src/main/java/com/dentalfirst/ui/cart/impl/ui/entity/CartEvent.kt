package com.dentalfirst.ui.cart.impl.ui.entity

internal sealed interface CartEvent {
    data class ShowToast(val message: String) : CartEvent
}
