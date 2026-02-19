package com.dentalfirst.ui.cart.impl.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.dentalfirst.base.ui.R
import com.dentalfirst.ui.cart.impl.ui.entity.CartCallbacks
import com.dentalfirst.ui.cart.impl.ui.entity.CartContentUiState
import com.dentalfirst.ui.cart.impl.ui.entity.CartItemUiState
import com.dentalfirst.ui.cart.impl.ui.entity.CartScreenUiState
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import java.text.NumberFormat
import java.util.Locale

private val CardControlSize = 36.dp
private val CardControlCorner = RoundedCornerShape(10.dp)
private val ControlIconSize = 22.dp
private val CardItemShape = RoundedCornerShape(16.dp)
private val CardControlSpacing = 8.dp
private val CardBorderColor = Color(0xFFE5E7EB)
private val CardControlBackground = Color(0xFFF3F4F6)
private val CardPrimaryControlBackground = Color(0xFFE8ECFF)

@Composable
internal fun CartScreenView(
    uiState: CartScreenUiState,
    callbacks: CartCallbacks,
) {
    when (uiState) {
        CartScreenUiState.Loading -> LoadingState()
        CartScreenUiState.Empty -> EmptyState(onOpenCatalog = callbacks.onOpenCatalog)
        is CartScreenUiState.Content -> ContentState(
            state = uiState.state,
            onToggleAll = callbacks.onToggleAll,
            onToggleItem = callbacks.onToggleItem,
            onIncrease = callbacks.onIncrease,
            onDecrease = callbacks.onDecrease,
            onRemove = callbacks.onRemove,
            onCheckout = callbacks.onCheckout,
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Загрузка корзины...")
    }
}

@Composable
private fun EmptyState(
    onOpenCatalog: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_box),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(100.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Корзина",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
            text = "Сейчас в вашей корзине пусто, перейдите в каталог, добавьте товары в корзину и возвращайтесь чтобы оформить заказ",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
            onClick = onOpenCatalog,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 20.dp),
                text = "Каталог товаров",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun ContentState(
    state: CartContentUiState,
    onToggleAll: () -> Unit,
    onToggleItem: (Long) -> Unit,
    onIncrease: (Long) -> Unit,
    onDecrease: (Long) -> Unit,
    onRemove: (Long) -> Unit,
    onCheckout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .systemBarsPadding()
            .padding(start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = "Корзина",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = "• ${state.totalItemsCount} всего товаров • ${formatRub(state.totalAmountRub)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onToggleAll() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SelectionMark(isSelected = state.allSelected)
                Text(
                    text = "Выбрать все",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
            ActionIconButton(
                icon = Icons.Default.Delete,
                activeIcon = Icons.Default.Delete,
                active = false,
            ) { /* todo bulk delete */ }
            Spacer(modifier = Modifier.width(CardControlSpacing))
            ActionIconButton(
                icon = Icons.Default.FavoriteBorder,
                activeIcon = Icons.Default.Favorite,
                active = false,
            ) { /* todo bulk favorite */ }
            Spacer(modifier = Modifier.width(CardControlSpacing))
            ActionIconButton(
                icon = Icons.Default.Share,
                activeIcon = Icons.Default.Share,
                active = false,
            ) { /* todo bulk share */ }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    bottom = 190.dp,
                ),
            ) {
                items(state.items, key = { it.productId }) { item ->
                    CartSwipeItem(
                        item = item,
                        onToggleItem = onToggleItem,
                        onIncrease = onIncrease,
                        onDecrease = onDecrease,
                        onRemove = onRemove,
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 104.dp),
                shape = CardItemShape,
                border = BorderStroke(1.dp, CardBorderColor),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = formatRub(state.selectedAmountRub),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "${state.selectedItemsCount} шт.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Button(
                        modifier = Modifier.height(48.dp),
                        onClick = onCheckout,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp,
                            vertical = 10.dp,
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White,
                        ),
                    ) {
                        Text(text = "Оформить заказ")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CartSwipeItem(
    item: CartItemUiState,
    onToggleItem: (Long) -> Unit,
    onIncrease: (Long) -> Unit,
    onDecrease: (Long) -> Unit,
    onRemove: (Long) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value != SwipeToDismissBoxValue.Settled) {
                onRemove(item.productId)
                true
            } else {
                false
            }
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CardItemShape)
                    .background(Color(0xFFFFDAD6))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color(0xFFB3261E),
                )
            }
        },
    ) {
        CartItemCard(
            item = item,
            onToggleItem = onToggleItem,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            onRemove = onRemove,
        )
    }
}

@Composable
private fun CartItemCard(
    item: CartItemUiState,
    onToggleItem: (Long) -> Unit,
    onIncrease: (Long) -> Unit,
    onDecrease: (Long) -> Unit,
    onRemove: (Long) -> Unit,
) {
    var isFavorite by rememberSaveable(item.productId) { mutableStateOf(false) }

    Card(
        modifier = Modifier.border(
            width = 1.dp,
            color = CardBorderColor,
            shape = CardItemShape,
        ),
        shape = CardItemShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.padding(end = 10.dp),
                ) {
                    ProductImage(
                        imageUrl = item.imageUrl,
                        title = item.title,
                    )
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable { onToggleItem(item.productId) },
                    ) {
                        SelectionMark(isSelected = item.isSelected)
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                ) {
                    Text(
                        text = "ID: ${item.productId}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                    Text(
                        text = "${formatRub(item.priceRub)} за шт.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    Text(
                        text = formatRub(item.lineTotalRub),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ActionIconButton(
                    icon = Icons.Default.Delete,
                    activeIcon = Icons.Default.Delete,
                    active = false,
                ) { onRemove(item.productId) }
                Spacer(modifier = Modifier.width(CardControlSpacing))
                ActionIconButton(
                    icon = Icons.Default.FavoriteBorder,
                    activeIcon = Icons.Default.Favorite,
                    active = isFavorite,
                ) { isFavorite = !isFavorite }
                Spacer(modifier = Modifier.width(CardControlSpacing))
                QuantityButton(icon = Icons.Default.Remove) {
                    onDecrease(item.productId)
                }
                Spacer(modifier = Modifier.width(CardControlSpacing))
                Text(
                    text = "${item.quantity} шт.",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.width(CardControlSpacing))
                QuantityButton(icon = Icons.Default.Add) {
                    onIncrease(item.productId)
                }
                Spacer(modifier = Modifier.weight(1f))
                BuyButton()
            }
        }
    }
}

@Composable
private fun ActionIconButton(
    icon: ImageVector,
    activeIcon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val tint = if (active || isPressed) MaterialTheme.colorScheme.primary else Color(0xFF9CA3AF)

    Box(
        modifier = Modifier
            .size(CardControlSize)
            .clip(CardControlCorner)
            .background(CardControlBackground)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (active) activeIcon else icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(ControlIconSize),
        )
    }
}

@Composable
private fun ProductImage(
    imageUrl: String?,
    title: String,
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = title,
        modifier = Modifier
            .size(96.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardControlBackground),
        contentScale = ContentScale.Crop,
        loading = { PlaceholderImageContent() },
        error = { PlaceholderImageContent() },
        success = { SubcomposeAsyncImageContent() },
    )
}

@Composable
private fun PlaceholderImageContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_box),
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(34.dp),
        )
    }
}

@Composable
private fun SelectionMark(isSelected: Boolean) {
    val shape = RoundedCornerShape(10.dp)
    val fillColor = if (isSelected) Color(0xFFFF8A00) else Color.White
    val borderColor = if (isSelected) Color(0xFFFF8A00) else Color(0xFFD1D5DB)

    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(shape)
            .background(fillColor)
            .border(width = 1.dp, color = borderColor, shape = shape),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
private fun QuantityButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(CardControlSize)
            .clip(CardControlCorner)
            .background(CardPrimaryControlBackground)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(ControlIconSize),
        )
    }
}

@Composable
private fun BuyButton() {
    Box(
        modifier = Modifier
            .height(CardControlSize)
            .clip(CardControlCorner)
            .background(CardPrimaryControlBackground)
            .clickable { /* design-only button */ }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Купить",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

private fun formatRub(value: Int): String {
    val formatted = NumberFormat
        .getIntegerInstance(Locale.Builder().setLanguage("ru").setRegion("RU").build())
        .format(value)
    return "$formatted ₽"
}
