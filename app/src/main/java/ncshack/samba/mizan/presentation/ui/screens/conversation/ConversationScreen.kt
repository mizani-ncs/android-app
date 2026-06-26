package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.presentation.viewmodel.ConversationItem
import ncshack.samba.mizan.presentation.viewmodel.ConversationIntent
import ncshack.samba.mizan.presentation.viewmodel.ConversationUiState
import ncshack.samba.mizan.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onIntent: (ConversationIntent) -> Unit,
    onNavigateToLawyerProfile: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.items.size) {
        if (state.items.isNotEmpty()) {
            listState.animateScrollToItem(state.items.size - 1)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Lexify",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                items(
                    items = state.items,
                    key = { it.id },
                ) { item ->
                    when (item) {
                        is ConversationItem.UserMessage -> MessageBubble(
                            text = item.text,
                            isUser = true,
                        )
                        is ConversationItem.CardGroup -> CardGroup(
                            cards = item.cards,
                            onCardActionTapped = { cardId ->
                                onIntent(ConversationIntent.CardActionTapped(cardId))
                            },
                            onNavigateToLawyerProfile = onNavigateToLawyerProfile,
                            onNavigateToBooking = onNavigateToBooking,
                        )
                    }
                }

                if (state.isAwaitingResponse) {
                    item { TypingIndicator() }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = Dimens.CardRadius, topEnd = Dimens.CardRadius),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
            ) {
                MessageInput(
                    value = state.inputText,
                    onValueChange = { onIntent(ConversationIntent.TextChanged(it)) },
                    onSubmit = { onIntent(ConversationIntent.Submit) },
                    enabled = !state.isAwaitingResponse,
                )
            }

            Spacer(modifier = Modifier.height(34.dp))
        }
    }
}
