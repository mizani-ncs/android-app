package ncshack.samba.mizan.presentation.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material3.SnackbarHostState
import ncshack.samba.mizan.presentation.viewmodel.AuthEffect
import ncshack.samba.mizan.presentation.viewmodel.AuthEvent
import ncshack.samba.mizan.presentation.viewmodel.AuthUiState
import kotlinx.coroutines.flow.Flow

@Composable
fun AuthRoute(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    effect: Flow<AuthEffect>,
    onAuthSuccess: () -> Unit,
) {
    var isLogin by rememberSaveable { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effect.collect { e ->
            when (e) {
                is AuthEffect.NavigateToConversation -> onAuthSuccess()
                is AuthEffect.ShowError -> snackbarHostState.showSnackbar(e.message)
            }
        }
    }

    AuthScreen(
        state = state,
        onEvent = onEvent,
        isLogin = isLogin,
        onToggleMode = { isLogin = !isLogin },
        snackbarHostState = snackbarHostState,
    )
}
