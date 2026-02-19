package com.dentalfirst.ui.cart.impl.ui.entity

internal sealed interface CartScreenUiState {
    data object Loading : CartScreenUiState
    data object Empty : CartScreenUiState
    data class Content(val state: CartContentUiState) : CartScreenUiState
}

internal data class CartContentUiState(
    val items: List<CartItemUiState>,
    val totalItemsCount: Int,
    val totalAmountRub: Int,
    val selectedItemsCount: Int,
    val selectedAmountRub: Int,
    val allSelected: Boolean,
)

internal data class CartItemUiState(
    val productId: Long,
    val title: String,
    val imageUrl: String?,
    val priceRub: Int,
    val quantity: Int,
    val lineTotalRub: Int,
    val isSelected: Boolean,
)
