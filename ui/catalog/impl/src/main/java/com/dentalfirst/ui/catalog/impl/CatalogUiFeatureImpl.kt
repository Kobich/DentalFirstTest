package com.dentalfirst.ui.catalog.impl

import androidx.compose.runtime.Composable
import com.dentalfirst.feature.cart.api.CartFeature
import com.dentalfirst.ui.catalog.api.CatalogUiFeature
import com.dentalfirst.ui.catalog.impl.ui.CatalogScreen

class CatalogUiFeatureImpl(
    private val cartFeature: CartFeature,
) : CatalogUiFeature {
    @Composable
    override fun Content() {
        CatalogScreen(cartFeature = cartFeature)
    }
}
