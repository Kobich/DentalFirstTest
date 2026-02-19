package com.dentalfirst.ui.cart.impl

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dentalfirst.feature.cart.api.CartFeature
import com.dentalfirst.ui.cart.api.CartUiFeature
import com.dentalfirst.ui.cart.impl.ui.CartScreen
import com.dentalfirst.ui.cart.impl.ui.CartViewModel

class CartUiFeatureImpl(
    private val cartFeature: CartFeature,
) : CartUiFeature {
    @Composable
    override fun Content(onOpenCatalog: () -> Unit) {
        val viewModel: CartViewModel = viewModel(
            factory = CartViewModel.factory(cartFeature),
        )
        CartScreen(
            viewModel = viewModel,
            onOpenCatalog = onOpenCatalog,
        )
    }
}
