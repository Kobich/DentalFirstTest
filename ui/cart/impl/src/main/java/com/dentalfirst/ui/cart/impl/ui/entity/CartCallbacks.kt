package com.dentalfirst.ui.cart.impl.ui.entity

internal data class CartCallbacks(
    val onOpenCatalog: () -> Unit,
    val onToggleAll: () -> Unit,
    val onToggleItem: (Long) -> Unit,
    val onIncrease: (Long) -> Unit,
    val onDecrease: (Long) -> Unit,
    val onRemove: (Long) -> Unit,
    val onCheckout: () -> Unit,
)
