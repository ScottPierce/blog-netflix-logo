package dev.scottpierce.netflixlogo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
                        // Wait before animating the logo in
                        delay(1.seconds)
                        drawLogo = true
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        Box(
//                            contentAlignment = Alignment.BottomCenter,
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Image(
//                                painterResource(id = R.drawable.netflix_symbol_official),
//                                contentDescription = null,
//                                modifier = Modifier.width(400.dp),
//                                contentScale = ContentScale.FillWidth,
//                            )
//                        }

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (drawLogo) {
                                NetflixLogo(
                                    animated = true,
                                    modifier = Modifier.width(150.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
