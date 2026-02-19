package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.api.ProductQuantity
import com.dentalfirst.feature.cart.impl.data.CartRepository

internal class AddProductsUseCase(
    private val repository: CartRepository,
) {
    suspend operator fun invoke(products: List<ProductQuantity>) {
        repository.addProducts(products)
    }
}
