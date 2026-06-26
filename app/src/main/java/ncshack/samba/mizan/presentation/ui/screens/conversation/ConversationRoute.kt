package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import ncshack.samba.mizan.presentation.viewmodel.ConversationEffect
import ncshack.samba.mizan.presentation.viewmodel.ConversationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConversationRoute(
    viewModel: ConversationViewModel = koinViewModel(),
    onNavigateToLawyerProfile: (String) -> Unit = {},
    onNavigateToBooking: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConversationEffect.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Short,
                    )
                }
                is ConversationEffect.SessionStarted -> {
//                    snackbarHostState.showSnackbar(
//                        message = "Session started",
//                        duration = SnackbarDuration.Short,
//                    )
                }
            }
        }
    }

    ConversationScreen(
        state = state,
        onIntent = viewModel::onEvent,
        snackbarHostState = snackbarHostState,
        onNavigateToLawyerProfile = onNavigateToLawyerProfile,
        onNavigateToBooking = onNavigateToBooking,
    )
}
