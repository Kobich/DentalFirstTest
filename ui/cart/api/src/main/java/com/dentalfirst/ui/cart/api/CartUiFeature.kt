package com.dentalfirst.ui.cart.api

import androidx.compose.runtime.Composable

interface CartUiFeature {
    @Composable
    fun Content(onOpenCatalog: () -> Unit)
}
