package ncshack.samba.mizan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import ncshack.samba.mizan.navigation.NavGraph
import ncshack.samba.mizan.presentation.viewmodel.AuthViewModel
import ncshack.samba.mizan.ui.theme.MizanTheme
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MizanTheme {
                MizanAppContent()
            }
        }
    }
}

@Composable
fun MizanAppContent() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        NavGraph(
            navController = navController,
            authViewModel = authViewModel,
        )
    }
}
