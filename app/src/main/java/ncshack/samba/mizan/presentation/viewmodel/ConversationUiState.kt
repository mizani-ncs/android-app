package ncshack.samba.mizan.presentation.viewmodel

import ncshack.samba.mizan.domain.model.Session

data class ConversationUiState(
    val items: List<ConversationItem> = emptyList(),
    val inputText: String = "",
    val isAwaitingResponse: Boolean = false,
    val error: String? = null,
    val sessions: List<Session> = emptyList(),
    val currentSessionId: String? = null,
    val showSidebar: Boolean = false,
    val isLoadingSessions: Boolean = false,
)
