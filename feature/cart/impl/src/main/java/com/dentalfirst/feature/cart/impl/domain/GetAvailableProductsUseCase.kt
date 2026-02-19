package com.dentalfirst.feature.cart.impl.domain

import com.dentalfirst.feature.cart.api.Product
import com.dentalfirst.feature.cart.impl.data.catalog.CatalogDataSource

internal class GetAvailableProductsUseCase(
    private val catalogDataSource: CatalogDataSource,
) {
    suspend operator fun invoke(): List<Product> = catalogDataSource.getProducts()
}
