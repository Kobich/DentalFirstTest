package com.dentalfirst.ui.catalog.impl.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dentalfirst.feature.cart.api.CartFeature
import kotlinx.coroutines.launch

@Composable
internal fun CatalogScreen(
    cartFeature: CartFeature,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Каталог",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 24.dp),
            text = "Для демо нажмите кнопку, чтобы добавить товары в корзину.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onClick = {
                scope.launch {
                    cartFeature.fillWithDemoProducts(count = 4)
                    Toast.makeText(context, "Товары добавлены в корзину", Toast.LENGTH_SHORT).show()
                }
            },
        ) {
            Text(
                text = "Заполнить корзину",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 10.dp),
            )
        }
    }
}
