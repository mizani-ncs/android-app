package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ncshack.samba.mizan.presentation.viewmodel.ConversationEffect
import ncshack.samba.mizan.presentation.viewmodel.ConversationIntent
import ncshack.samba.mizan.presentation.viewmodel.ConversationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConversationRoute(
    viewModel: ConversationViewModel = koinViewModel(),
    onNavigateToLawyerProfile: (String) -> Unit = {},
    onNavigateToBooking: (String) -> Unit = {},
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ConversationEffect.ShowError -> {
                    // Snackbar handled by parent
                }
            }
        }
    }

    ConversationScreen(
        state = state,
        onIntent = viewModel::onEvent,
        onNavigateToLawyerProfile = onNavigateToLawyerProfile,
        onNavigateToBooking = onNavigateToBooking,
    )
}
