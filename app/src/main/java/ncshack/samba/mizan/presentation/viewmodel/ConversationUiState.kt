package ncshack.samba.mizan.presentation.viewmodel

data class ConversationUiState(
    val items: List<ConversationItem> = emptyList(),
    val inputText: String = "",
    val isAwaitingResponse: Boolean = false,
    val error: String? = null,
)
