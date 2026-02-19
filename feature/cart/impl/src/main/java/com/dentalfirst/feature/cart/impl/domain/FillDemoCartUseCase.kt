package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.api.ProductQuantity
import com.dentalfirst.feature.cart.impl.data.CartRepository
import com.dentalfirst.feature.cart.impl.data.catalog.CatalogDataSource

internal class FillDemoCartUseCase(
    private val repository: CartRepository,
    private val catalogDataSource: CatalogDataSource,
) {
    suspend operator fun invoke(count: Int) {
        if (count <= 0) return

        val demoProducts = catalogDataSource
            .getProducts()
            .take(count)
            .map { product ->
                ProductQuantity(productId = product.id, quantity = 1)
            }

        repository.addProducts(demoProducts)
    }
}
