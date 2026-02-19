package com.dentalfirst.feature.cart.impl.data.catalog

import android.content.Context
import com.dentalfirst.feature.cart.api.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

internal class MockCatalogDataSource(
    private val context: Context,
) : CatalogDataSource {

    private val cacheMutex = Mutex()
    private var cachedProducts: List<Product>? = null

    override suspend fun getProducts(): List<Product> {
        cachedProducts?.let { return it }

        return cacheMutex.withLock {
            cachedProducts?.let { return@withLock it }

            val parsed = withContext(Dispatchers.IO) {
                val jsonText = context.assets.open(PRODUCTS_FILE_NAME)
                    .bufferedReader()
                    .use { it.readText() }
                parseProducts(jsonText)
            }

            cachedProducts = parsed
            parsed
        }
    }

    private fun parseProducts(rawJson: String): List<Product> {
        val root = JSONObject(rawJson)
        val unique = linkedMapOf<Long, Product>()

        fun addProductFromObject(obj: JSONObject) {
            if (!obj.has("id") || !obj.has("price")) return

            val id = obj.optLong("id", Long.MIN_VALUE)
            val name = obj.optString("name", "")
            val price = obj.optInt("price", -1)
            if (id == Long.MIN_VALUE || name.isBlank() || price < 0) return

            unique[id] = Product(
                id = id,
                name = name,
                imageUrl = obj.takeIf { it.has("image") }?.optString("image"),
                priceRub = price,
            )
        }

        fun traverseArray(array: JSONArray?) {
            if (array == null) return

            for (index in 0 until array.length()) {
                val node = array.optJSONObject(index) ?: continue
                addProductFromObject(node)
                traverseArray(node.optJSONArray("items"))
            }
        }

        traverseArray(root.optJSONArray("products"))
        traverseArray(root.optJSONArray("catalog"))

        return unique.values.toList()
    }

    private companion object {
        const val PRODUCTS_FILE_NAME = "products.json"
    }
}
