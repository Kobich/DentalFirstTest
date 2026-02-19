package com.dentalfirst.ui.cart.impl.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.dentalfirst.ui.cart.impl.ui.entity.CartCallbacks
import com.dentalfirst.ui.cart.impl.ui.entity.CartEvent

@Composable
internal fun CartScreen(
    viewModel: CartViewModel,
    onOpenCatalog: () -> Unit,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is CartEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val callbacks = CartCallbacks(
        onOpenCatalog = onOpenCatalog,
        onToggleAll = viewModel::onToggleAll,
        onToggleItem = viewModel::onToggleItem,
        onIncrease = viewModel::onIncrease,
        onDecrease = viewModel::onDecrease,
        onRemove = viewModel::onRemove,
        onCheckout = viewModel::onCheckoutClicked,
    )

    CartScreenView(
        uiState = uiState,
        callbacks = callbacks,
    )
}
