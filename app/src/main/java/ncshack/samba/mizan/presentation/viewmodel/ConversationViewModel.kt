package ncshack.samba.mizan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ncshack.samba.mizan.data.remote.WidgetPushDataSource
import ncshack.samba.mizan.domain.model.CardDescriptor
import ncshack.samba.mizan.domain.repository.AuthRepository
import ncshack.samba.mizan.domain.usecase.PromptUseCase

class ConversationViewModel(
    private val promptUseCase: PromptUseCase,
    private val widgetPushDataSource: WidgetPushDataSource,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ConversationUiState())
    val state: StateFlow<ConversationUiState> = _state.asStateFlow()

    private val _effect = Channel<ConversationEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private val sessionId: String
        get() = authRepository.getSessionId().orEmpty()

    init {
        subscribeToWidgetPush()
    }

    fun onEvent(event: ConversationIntent) {
        when (event) {
            is ConversationIntent.TextChanged -> _state.update {
                it.copy(inputText = event.text)
            }
            is ConversationIntent.Submit -> submit()
            is ConversationIntent.CardActionTapped -> {
                // Card-specific actions handled in card composables
            }
            is ConversationIntent.ChipSelected -> {
                _state.update { it.copy(inputText = event.chipText) }
                submit()
            }
        }
    }

    private fun submit() {
        val text = _state.value.inputText.trim()
        if (text.isEmpty() || _state.value.isAwaitingResponse) return

        val userMessage = ConversationItem.UserMessage(
            id = "user_${System.currentTimeMillis()}",
            text = text,
        )

        _state.update {
            it.copy(
                items = it.items + userMessage,
                inputText = "",
                isAwaitingResponse = true,
            )
        }

        viewModelScope.launch {
            try {
                promptUseCase(sessionId = sessionId, text = text)
            } catch (e: Exception) {
                _state.update { it.copy(isAwaitingResponse = false) }
                _effect.send(ConversationEffect.ShowError(e.message ?: "Failed to send"))
            }
        }
    }

    private fun subscribeToWidgetPush() {
        viewModelScope.launch {
            widgetPushDataSource.subscribe(sessionId).collect { card ->
                val cardGroup = ConversationItem.CardGroup(
                    id = "card_${card.id}",
                    cards = listOf(card),
                )
                _state.update {
                    it.copy(
                        items = it.items + cardGroup,
                        isAwaitingResponse = false,
                    )
                }
            }
        }
    }
}
