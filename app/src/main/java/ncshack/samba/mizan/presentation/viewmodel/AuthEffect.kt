package ncshack.samba.mizan.presentation.viewmodel

sealed interface AuthEffect {
    data object NavigateToConversation : AuthEffect
    data class ShowError(val message: String) : AuthEffect
}
