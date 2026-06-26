package ncshack.samba.mizan.presentation.viewmodel

import ncshack.samba.mizan.domain.model.CardDescriptor

sealed interface ConversationItem {
    val id: String

    data class UserMessage(
        override val id: String,
        val text: String,
    ) : ConversationItem

    data class CardGroup(
        override val id: String,
        val cards: List<CardDescriptor>,
    ) : ConversationItem
}
