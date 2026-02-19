package com.dentalfirst.ui.cart.impl.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dentalfirst.feature.cart.api.CartFeature
import com.dentalfirst.ui.cart.impl.domain.CartInteractor
import com.dentalfirst.ui.cart.impl.ui.entity.CartEvent
import com.dentalfirst.ui.cart.impl.ui.entity.CartScreenUiState
import com.dentalfirst.ui.cart.impl.ui.mapper.CartUiMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CartViewModel(
    private val interactor: CartInteractor,
    private val mapper: CartUiMapper,
) : ViewModel() {

    private val eventsMutable = MutableSharedFlow<CartEvent>()
    val events = eventsMutable.asSharedFlow()
    private val deselectedIdsMutable = MutableStateFlow<Set<Long>>(emptySet())

    val uiState: StateFlow<CartScreenUiState> = combine(
        interactor.observeState(),
        deselectedIdsMutable,
    ) { domainState, deselectedIds ->
        val compactDeselected = when (domainState) {
            is com.dentalfirst.ui.cart.impl.domain.entity.CartDomainState.Content -> {
                val validIds = domainState.cartState.items.map { it.product.id }.toSet()
                deselectedIds.intersect(validIds)
            }

            else -> emptySet()
        }
        if (compactDeselected != deselectedIds) {
            deselectedIdsMutable.value = compactDeselected
        }
        mapper.map(domainState = domainState, deselectedIds = compactDeselected)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = CartScreenUiState.Loading,
        )

    fun onIncrease(productId: Long) {
        viewModelScope.launch {
            interactor.increase(productId)
        }
    }

    fun onDecrease(productId: Long) {
        viewModelScope.launch {
            interactor.decrease(productId)
        }
    }

    fun onRemove(productId: Long) {
        deselectedIdsMutable.update { it - productId }
        viewModelScope.launch {
            interactor.remove(productId)
        }
    }

    fun onToggleItem(productId: Long) {
        deselectedIdsMutable.update { ids ->
            if (ids.contains(productId)) ids - productId else ids + productId
        }
    }

    fun onToggleAll() {
        val state = uiState.value
        if (state !is CartScreenUiState.Content) return

        val allIds = state.state.items.map { it.productId }.toSet()
        deselectedIdsMutable.update { ids ->
            if (state.state.allSelected) {
                allIds
            } else {
                ids - allIds
            }
        }
    }

    fun onCheckoutClicked() {
        viewModelScope.launch {
            eventsMutable.emit(CartEvent.ShowToast("Функция в разработке"))
        }
    }

    companion object {
        fun factory(cartFeature: CartFeature): ViewModelProvider.Factory {
            val interactor = CartInteractor(cartFeature)
            val mapper = CartUiMapper()

            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CartViewModel(interactor = interactor, mapper = mapper) as T
                }
            }
        }
    }
}
