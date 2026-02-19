package com.dentalfirst.feature.cart.api

data class Product(
    val id: Long,
    val name: String,
    val imageUrl: String?,
    val priceRub: Int,
)

data class CartItem(
    val product: Product,
    val quantity: Int,
) {
    val lineTotalRub: Int
        get() = product.priceRub * quantity
}

data class CartState(
    val items: List<CartItem>,
) {
    val totalItemsCount: Int
        get() = items.sumOf { it.quantity }

    val totalAmountRub: Int
        get() = items.sumOf { it.lineTotalRub }
}

data class ProductQuantity(
    val productId: Long,
    val quantity: Int,
)
