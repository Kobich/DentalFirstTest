package com.dentalfirst.feature.cart.impl.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.cartDataStore by preferencesDataStore(name = "cart_data_store")

internal class DataStoreCartStorage(
    private val context: Context,
) : CartStorage {

    override fun observeQuantities(): Flow<Map<Long, Int>> {
        return context.cartDataStore.data.map { preferences ->
            parse(preferences[CART_DATA_KEY].orEmpty())
        }
    }

    override suspend fun updateQuantities(transform: (MutableMap<Long, Int>) -> Unit) {
        context.cartDataStore.edit { preferences ->
            val mutable = parse(preferences[CART_DATA_KEY].orEmpty()).toMutableMap()
            transform(mutable)
            preferences[CART_DATA_KEY] = serialize(mutable)
        }
    }

    private fun parse(raw: String): Map<Long, Int> {
        if (raw.isBlank()) return emptyMap()

        return raw
            .split(ENTRY_SEPARATOR)
            .mapNotNull { entry ->
                val chunks = entry.split(KEY_VALUE_SEPARATOR)
                if (chunks.size != 2) return@mapNotNull null

                val productId = chunks[0].toLongOrNull() ?: return@mapNotNull null
                val quantity = chunks[1].toIntOrNull() ?: return@mapNotNull null

                if (quantity <= 0) return@mapNotNull null
                productId to quantity
            }
            .toMap()
    }

    private fun serialize(quantities: Map<Long, Int>): String {
        return quantities
            .filterValues { it > 0 }
            .toSortedMap()
            .entries
            .joinToString(ENTRY_SEPARATOR) { (productId, quantity) ->
                "$productId$KEY_VALUE_SEPARATOR$quantity"
            }
    }

    private companion object {
        val CART_DATA_KEY = stringPreferencesKey("cart_data")
        const val ENTRY_SEPARATOR = ";"
        const val KEY_VALUE_SEPARATOR = ":"
    }
}
