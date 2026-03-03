package il.soulSalttrader.shabbattimes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import il.soulSalttrader.shabbattimes.content.shabbat.ShabbatScaffold
import il.soulSalttrader.shabbattimes.nav.NavApp
import il.soulSalttrader.shabbattimes.nav.Navigator
import il.soulSalttrader.shabbattimes.ui.theme.ShabbatTheme
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var navigator: Navigator

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            ShabbatTheme {
                ShabbatScaffold(navigator = navigator) { innerPadding ->
                    NavApp(
                        modifier = Modifier.padding(innerPadding),
                        navigator = navigator,
                    )
                }
            }
        }
    }
}