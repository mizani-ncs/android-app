package ncshack.samba.mizan.presentation.viewmodel

sealed interface ConversationIntent {
    data class TextChanged(val text: String) : ConversationIntent
    data object Submit : ConversationIntent
    data class CardActionTapped(val text: String) : ConversationIntent
    data class ChipSelected(val chipText: String) : ConversationIntent
    data object ToggleSidebar : ConversationIntent
    data class SelectSession(val sessionId: String) : ConversationIntent
    data object NewSession : ConversationIntent
    data object DismissError : ConversationIntent
    data object Logout : ConversationIntent
}
