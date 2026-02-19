package com.dentalfirst.feature.cart.impl

import com.dentalfirst.feature.cart.api.CartFeature
import com.dentalfirst.feature.cart.api.CartItem
import com.dentalfirst.feature.cart.api.CartState
import com.dentalfirst.feature.cart.api.Product
import com.dentalfirst.feature.cart.api.ProductQuantity
import com.dentalfirst.feature.cart.impl.domain.AddProductUseCase
import com.dentalfirst.feature.cart.impl.domain.AddProductsUseCase
import com.dentalfirst.feature.cart.impl.domain.ChangeQuantityUseCase
import com.dentalfirst.feature.cart.impl.domain.FillDemoCartUseCase
import com.dentalfirst.feature.cart.impl.domain.GetAvailableProductsUseCase
import com.dentalfirst.feature.cart.impl.domain.ObserveCartUseCase
import com.dentalfirst.feature.cart.impl.domain.RemoveProductUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class CartFeatureImpl(
    private val observeCartUseCase: ObserveCartUseCase,
    private val getAvailableProductsUseCase: GetAvailableProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val addProductsUseCase: AddProductsUseCase,
    private val changeQuantityUseCase: ChangeQuantityUseCase,
    private val removeProductUseCase: RemoveProductUseCase,
    private val fillDemoCartUseCase: FillDemoCartUseCase,
) : CartFeature {

    override fun observeCart(): Flow<CartState> {
        return observeCartUseCase().map { quantities ->
            val productsById = getAvailableProductsUseCase().associateBy(Product::id)
            val items = quantities.entries
                .sortedBy { it.key }
                .mapNotNull { (productId, quantity) ->
                    productsById[productId]?.let { product ->
                        CartItem(product = product, quantity = quantity)
                    }
                }
            CartState(items = items)
        }
    }

    override suspend fun getAvailableProducts(): List<Product> = getAvailableProductsUseCase()

    override suspend fun addProduct(productId: Long, quantity: Int) {
        addProductUseCase(productId, quantity)
    }

    override suspend fun addProducts(products: List<ProductQuantity>) {
        addProductsUseCase(products)
    }

    override suspend fun changeQuantity(productId: Long, delta: Int) {
        changeQuantityUseCase(productId, delta)
    }

    override suspend fun removeProduct(productId: Long) {
        removeProductUseCase(productId)
    }

    override suspend fun fillWithDemoProducts(count: Int) {
        fillDemoCartUseCase(count)
    }
}
