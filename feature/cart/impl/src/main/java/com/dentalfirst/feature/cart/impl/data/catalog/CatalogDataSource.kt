package com.dentalfirst.feature.cart.impl.data.catalog

import com.dentalfirst.feature.cart.api.Product

internal interface CatalogDataSource {
    suspend fun getProducts(): List<Product>
}
