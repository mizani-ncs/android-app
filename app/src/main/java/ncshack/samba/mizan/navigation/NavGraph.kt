package ncshack.samba.mizan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ncshack.samba.mizan.presentation.ui.screens.auth.AuthRoute
import ncshack.samba.mizan.presentation.ui.screens.conversation.ConversationRoute
import ncshack.samba.mizan.presentation.viewmodel.AuthViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
) {
    val state by authViewModel.state.collectAsStateWithLifecycle()
    val startDestination = if (state.isAuthenticated) Conversation else Auth

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Auth> {
            AuthRoute(
                state = state,
                onEvent = authViewModel::onEvent,
                effect = authViewModel.effect,
                onAuthSuccess = {
                    navController.navigate(Conversation) {
                        popUpTo<Auth> { inclusive = true }
                    }
                },
            )
        }

        composable<Conversation> {
            ConversationRoute(
                onNavigateToLawyerProfile = { lawyerId ->
                    navController.navigate(LawyerProfile(lawyerId))
                },
                onNavigateToBooking = { lawyerId ->
                    navController.navigate(Booking(lawyerId))
                },
                onNavigateToAuth = {
                    navController.navigate(Auth) {
                        popUpTo<Conversation> { inclusive = true }
                    }
                },
            )
        }
    }
}
