package com.dentalfirst

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.dentalfirst.base.ui.R as BaseUiR
import com.dentalfirst.feature.cart.impl.CartFeatureFactory
import com.dentalfirst.ui.catalog.api.CatalogUiFeature
import com.dentalfirst.ui.catalog.impl.CatalogUiFeatureImpl
import com.dentalfirst.ui.cart.api.CartUiFeature
import com.dentalfirst.ui.cart.impl.CartUiFeatureImpl
import com.dentalfirst.ui.theme.DentalFirstTheme
import com.dentalfirst.ui.theme.MainBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cartFeature = CartFeatureFactory.create(applicationContext)
        val cartUiFeature: CartUiFeature = CartUiFeatureImpl(cartFeature)
        val catalogUiFeature: CatalogUiFeature = CatalogUiFeatureImpl(cartFeature)

        setContent {
            DentalFirstTheme {
                DentalAppRoot(
                    cartUiFeature = cartUiFeature,
                    catalogUiFeature = catalogUiFeature,
                )
            }
        }
    }
}

@Composable
private fun DentalAppRoot(
    cartUiFeature: CartUiFeature,
    catalogUiFeature: CatalogUiFeature,
) {
    var selectedTab by rememberSaveable { mutableStateOf(AppTab.Cart) }
    val isCartTab = selectedTab == AppTab.Cart
    val barsColor = if (isCartTab) MainBlue else MaterialTheme.colorScheme.surface
    val scaffoldColor = if (isCartTab) MainBlue else MaterialTheme.colorScheme.background

    SystemBarsEffect(
        color = barsColor,
        useDarkIcons = !isCartTab,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = scaffoldColor,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            AppBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        },
    ) { innerPadding ->
        AppContent(
            selectedTab = selectedTab,
            innerPadding = innerPadding,
            cartUiFeature = cartUiFeature,
            catalogUiFeature = catalogUiFeature,
            onOpenCatalog = { selectedTab = AppTab.Catalog },
        )
    }
}

@Composable
private fun SystemBarsEffect(
    color: Color,
    useDarkIcons: Boolean,
) {
    val view = LocalView.current

    SideEffect {
        val activity = view.context as? Activity ?: return@SideEffect
        val window = activity.window

        window.statusBarColor = color.toArgb()
        window.navigationBarColor = color.toArgb()
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = useDarkIcons
            isAppearanceLightNavigationBars = useDarkIcons
        }
    }
}

@Composable
private fun AppBottomBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(horizontal = 12.dp, vertical = 20.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        NavigationBar(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(24.dp),
                ),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            AppTab.entries.forEach { tab ->
                NavigationBarItem(
                    selected = selectedTab == tab,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Icon(
                            painter = painterResource(id = tab.icon),
                            contentDescription = tab.label,
                        )
                    },
                    label = { Text(text = tab.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                )
            }
        }
    }
}

@Composable
private fun AppContent(
    selectedTab: AppTab,
    innerPadding: androidx.compose.foundation.layout.PaddingValues,
    cartUiFeature: CartUiFeature,
    catalogUiFeature: CatalogUiFeature,
    onOpenCatalog: () -> Unit,
) {
    val contentModifier = if (selectedTab == AppTab.Cart) {
        Modifier.fillMaxSize()
    } else {
        Modifier
            .fillMaxSize()
            .padding(innerPadding)
    }

    Column(modifier = contentModifier) {
        when (selectedTab) {
            AppTab.Home -> PlaceholderScreen(title = "Главная")
            AppTab.Catalog -> catalogUiFeature.Content()
            AppTab.Actions -> PlaceholderScreen(title = "Действия")
            AppTab.Cart -> cartUiFeature.Content(onOpenCatalog = onOpenCatalog)
            AppTab.Profile -> PlaceholderScreen(title = "Профиль")
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Экран пока без реализации",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private enum class AppTab(
    val label: String,
    val icon: Int,
) {
    Home(label = "Главная", icon = BaseUiR.drawable.home_angle),
    Catalog(label = "Каталог", icon = BaseUiR.drawable.widget_5),
    Actions(label = "Действия", icon = BaseUiR.drawable.add_square),
    Cart(label = "Корзина", icon = BaseUiR.drawable.ic_cart),
    Profile(label = "Профиль", icon = BaseUiR.drawable.user_rounded),
}
