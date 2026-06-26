package ncshack.samba.mizan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.data.remote.WidgetPushDataSource
import ncshack.samba.mizan.domain.usecase.GetMySessionsUseCase
import ncshack.samba.mizan.domain.usecase.PromptUseCase
import ncshack.samba.mizan.domain.usecase.PromptsBySessionUseCase
import ncshack.samba.mizan.domain.usecase.StartSessionUseCase

class ConversationViewModel(
    private val promptUseCase: PromptUseCase,
    private val widgetPushDataSource: WidgetPushDataSource,
    private val startSessionUseCase: StartSessionUseCase,
    private val getMySessionsUseCase: GetMySessionsUseCase,
    private val promptsBySessionUseCase: PromptsBySessionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ConversationUiState())
    val state: StateFlow<ConversationUiState> = _state.asStateFlow()

    private val _effect = Channel<ConversationEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var subscriptionJob: Job? = null

    init {
        initSession()
    }

    fun onEvent(event: ConversationIntent) {
        when (event) {
            is ConversationIntent.TextChanged -> _state.update {
                it.copy(inputText = event.text)
            }
            is ConversationIntent.Submit -> submit()
            is ConversationIntent.CardActionTapped -> {
                submit(event.text)
            }
            is ConversationIntent.ChipSelected -> {
                _state.update { it.copy(inputText = event.chipText) }
                submit()
            }
            is ConversationIntent.ToggleSidebar -> _state.update {
                it.copy(showSidebar = !it.showSidebar)
            }
            is ConversationIntent.SelectSession -> selectSession(event.sessionId)
            is ConversationIntent.NewSession -> initSession()
            is ConversationIntent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun initSession() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingSessions = true) }
            try {
                val session = startSessionUseCase()
                _state.update {
                    it.copy(
                        currentSessionId = session.id,
                        items = emptyList(),
                        isLoadingSessions = false,
                        showSidebar = false,
                    )
                }
                subscribeToWidgetPush(session.id)
                loadSessions()
                _effect.send(ConversationEffect.SessionStarted)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoadingSessions = false,
                        error = e.message ?: "Failed to start session",
                    )
                }
            }
        }
    }

    private fun selectSession(sessionId: String) {
        if (sessionId == _state.value.currentSessionId) return
        _state.update {
            it.copy(
                currentSessionId = sessionId,
                items = emptyList(),
                showSidebar = false,
            )
        }
        subscribeToWidgetPush(sessionId)
        loadHistory(sessionId)
    }

    private fun loadSessions() {
        viewModelScope.launch {
            try {
                val sessions = getMySessionsUseCase()
                _state.update { it.copy(sessions = sessions) }
            } catch (_: Exception) { /* silent */ }
        }
    }

    private fun loadHistory(sessionId: String) {
        viewModelScope.launch {
            try {
                val prompts = promptsBySessionUseCase(sessionId)
                val items = mutableListOf<ConversationItem>()
                for (prompt in prompts) {
                    if (!prompt.userText.isNullOrBlank()) {
                        items += ConversationItem.UserMessage(
                            id = "user_${prompt.id}",
                            text = prompt.userText,
                        )
                    }
                    if (prompt.cards.isNotEmpty()) {
                        items += ConversationItem.CardGroup(
                            id = "cards_${prompt.id}",
                            cards = prompt.cards,
                        )
                    }
                }
                _state.update { it.copy(items = items) }
            } catch (_: Exception) { /* silent */ }
        }
    }

    private fun submit(prompt: String? = null) {
        val text = (prompt ?: _state.value.inputText).trim()
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
                promptUseCase(
                    sessionId = _state.value.currentSessionId.orEmpty(),
                    text = text,
                )
            } catch (e: Exception) {
                _state.update { it.copy(isAwaitingResponse = false) }
                _effect.send(ConversationEffect.ShowError(e.message ?: "Failed to send"))
            }
        }
    }

    private fun subscribeToWidgetPush(sessionId: String) {
        subscriptionJob?.cancel()
        subscriptionJob = viewModelScope.launch(Dispatchers.IO) {
            widgetPushDataSource.subscribe(sessionId).collect { card ->
                if (card.cardType == "text") {
                    val text = try {
                        val json = kotlinx.serialization.json.Json.parseToJsonElement(card.payload)
                        json.jsonObject["text"]?.jsonPrimitive?.content.orEmpty()
                    } catch (_: Exception) { card.payload }
                    val aiMessage = ConversationItem.UserMessage(
                        id = "ai_${card.id}",
                        text = text,
                        isUser = false,
                    )
                    _state.update {
                        it.copy(
                            items = it.items + aiMessage,
                            isAwaitingResponse = false,
                        )
                    }
                } else {
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
}
