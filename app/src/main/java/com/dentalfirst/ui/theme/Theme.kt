package com.dentalfirst.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppLightColorScheme = lightColorScheme(
    primary = MainBlue,
    onPrimary = PureWhite,
    primaryContainer = SoftBlue,
    onPrimaryContainer = MainBlue,
    secondary = MainBlue,
    onSecondary = PureWhite,
    background = PureWhite,
    onBackground = Ink,
    surface = PureWhite,
    onSurface = Ink,
    surfaceVariant = PureWhite,
    onSurfaceVariant = MutedInk,
)

private val AppDarkColorScheme = darkColorScheme(
    primary = MainBlue,
    onPrimary = PureWhite,
    primaryContainer = SoftBlue,
    onPrimaryContainer = MainBlue,
    secondary = MainBlue,
    onSecondary = PureWhite,
    background = PureWhite,
    onBackground = Ink,
    surface = PureWhite,
    onSurface = Ink,
    surfaceVariant = PureWhite,
    onSurfaceVariant = MutedInk,
)

@Composable
fun DentalFirstTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) AppDarkColorScheme else AppLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
