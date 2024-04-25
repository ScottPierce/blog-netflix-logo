package dev.scottpierce.netflixlogo.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import dev.scottpierce.netflix.logo.NetflixLogo

private val DarkColorScheme = darkColorScheme(
    primary = Color.White,
    secondary = NetflixLogo.COLOR_RED,
    tertiary = NetflixLogo.COLOR_RED,
    background = Color.Black,
)

@Composable
fun NetflixLogoTheme(
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            dynamicDarkColorScheme(context)
        }

        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}