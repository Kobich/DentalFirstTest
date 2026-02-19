package com.dentalfirst.feature.cart.impl

import android.content.Context
import com.dentalfirst.feature.cart.api.CartFeature
import com.dentalfirst.feature.cart.impl.data.CartRepositoryImpl
import com.dentalfirst.feature.cart.impl.data.catalog.MockCatalogDataSource
import com.dentalfirst.feature.cart.impl.data.local.DataStoreCartStorage
import com.dentalfirst.feature.cart.impl.domain.AddProductUseCase
import com.dentalfirst.feature.cart.impl.domain.AddProductsUseCase
import com.dentalfirst.feature.cart.impl.domain.ChangeQuantityUseCase
import com.dentalfirst.feature.cart.impl.domain.FillDemoCartUseCase
import com.dentalfirst.feature.cart.impl.domain.GetAvailableProductsUseCase
import com.dentalfirst.feature.cart.impl.domain.ObserveCartUseCase
import com.dentalfirst.feature.cart.impl.domain.RemoveProductUseCase

object CartFeatureFactory {
    fun create(context: Context): CartFeature {
        val appContext = context.applicationContext
        val catalogDataSource = MockCatalogDataSource(appContext)
        val storage = DataStoreCartStorage(appContext)
        val repository = CartRepositoryImpl(storage)

        return CartFeatureImpl(
            observeCartUseCase = ObserveCartUseCase(repository),
            getAvailableProductsUseCase = GetAvailableProductsUseCase(catalogDataSource),
            addProductUseCase = AddProductUseCase(repository),
            addProductsUseCase = AddProductsUseCase(repository),
            changeQuantityUseCase = ChangeQuantityUseCase(repository),
            removeProductUseCase = RemoveProductUseCase(repository),
            fillDemoCartUseCase = FillDemoCartUseCase(repository, catalogDataSource),
        )
    }
}
