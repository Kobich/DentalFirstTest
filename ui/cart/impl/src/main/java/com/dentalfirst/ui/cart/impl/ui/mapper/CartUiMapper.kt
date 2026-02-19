package com.dentalfirst.ui.cart.impl.ui.mapper

import com.dentalfirst.ui.cart.impl.domain.entity.CartDomainState
import com.dentalfirst.ui.cart.impl.ui.entity.CartContentUiState
import com.dentalfirst.ui.cart.impl.ui.entity.CartItemUiState
import com.dentalfirst.ui.cart.impl.ui.entity.CartScreenUiState

internal class CartUiMapper {
    fun map(
        domainState: CartDomainState,
        deselectedIds: Set<Long>,
    ): CartScreenUiState {
        return when (domainState) {
            CartDomainState.Loading -> CartScreenUiState.Loading
            CartDomainState.Empty -> CartScreenUiState.Empty
            is CartDomainState.Content -> {
                val cartState = domainState.cartState
                val mappedItems = cartState.items.map { cartItem ->
                    CartItemUiState(
                        productId = cartItem.product.id,
                        title = cartItem.product.name,
                        imageUrl = cartItem.product.imageUrl,
                        priceRub = cartItem.product.priceRub,
                        quantity = cartItem.quantity,
                        lineTotalRub = cartItem.lineTotalRub,
                        isSelected = !deselectedIds.contains(cartItem.product.id),
                    )
                }
                val selectedItems = mappedItems.filter { it.isSelected }

                CartScreenUiState.Content(
                    state = CartContentUiState(
                        totalItemsCount = cartState.totalItemsCount,
                        totalAmountRub = cartState.totalAmountRub,
                        selectedItemsCount = selectedItems.sumOf { it.quantity },
                        selectedAmountRub = selectedItems.sumOf { it.lineTotalRub },
                        allSelected = mappedItems.all { it.isSelected },
                        items = mappedItems,
                    ),
                )
            }
        }
    }
}
