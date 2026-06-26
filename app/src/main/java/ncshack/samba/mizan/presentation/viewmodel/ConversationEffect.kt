package ncshack.samba.mizan.presentation.viewmodel

sealed interface ConversationEffect {
    data class ShowError(val message: String) : ConversationEffect
    data object SessionStarted : ConversationEffect
}
