package ncshack.samba.mizan.presentation.viewmodel

sealed interface ConversationIntent {
    data class TextChanged(val text: String) : ConversationIntent
    data object Submit : ConversationIntent
    data class CardActionTapped(val cardId: String) : ConversationIntent
    data class ChipSelected(val chipText: String) : ConversationIntent
}
