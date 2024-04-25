package dev.scottpierce.netflixlogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import dev.scottpierce.netflix.logo.NetflixLogo
import dev.scottpierce.netflixlogo.theme.NetflixLogoTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }

        setContent {
            NetflixLogoTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    var drawLogo: Boolean by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        // Wait 500 millis before animating the logo in
                        delay(1.seconds)
                        drawLogo = true
                    }

                    if (drawLogo) {
                        NetflixLogo(
                            animated = true,
                            modifier = Modifier.width(300.dp)
                        )
                    }
                }
            }
        }
    }
}
