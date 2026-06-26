package ncshack.samba.mizan.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.domain.model.CardDescriptor
import ncshack.samba.mizan.presentation.ui.screens.auth.AuthScreen
import ncshack.samba.mizan.presentation.ui.screens.conversation.BookingCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.ClarifyingChipsCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.ContextCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.DeadlineCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.DownloadFileCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.LawyerCarouselCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.MessageBubble
import ncshack.samba.mizan.presentation.ui.screens.conversation.MessageInput
import ncshack.samba.mizan.presentation.ui.screens.conversation.SubmitDocumentCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.TypingIndicator
import ncshack.samba.mizan.presentation.viewmodel.AuthUiState
import ncshack.samba.mizan.presentation.viewmodel.ConversationItem
import ncshack.samba.mizan.presentation.viewmodel.ConversationUiState
import ncshack.samba.mizan.ui.theme.MizanTheme

// ──────────────────────────────────────────────
// Auth
// ──────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Login")
@Composable
private fun AuthLoginPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(
                email = "user@example.com",
                password = "password123",
            ),
            onEvent = {},
            isLogin = true,
            onToggleMode = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Register")
@Composable
private fun AuthRegisterPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(
                email = "newuser@example.com",
                password = "securePass!",
                confirmPassword = "securePass!",
            ),
            onEvent = {},
            isLogin = false,
            onToggleMode = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Login Loading")
@Composable
private fun AuthLoginLoadingPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(
                email = "user@example.com",
                password = "password123",
                isLoading = true,
            ),
            onEvent = {},
            isLogin = true,
            onToggleMode = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Login with Errors")
@Composable
private fun AuthLoginErrorsPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(
                email = "bad-email",
                password = "123",
                errors = mapOf(
                    "email" to "Invalid email",
                    "password" to "Password must be at least 8 characters",
                ),
            ),
            onEvent = {},
            isLogin = true,
            onToggleMode = {},
        )
    }
}

// ──────────────────────────────────────────────
// Conversation
// ──────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Conversation — Empty")
@Composable
private fun ConversationEmptyPreview() {
    MizanTheme {
        ncshack.samba.mizan.presentation.ui.screens.conversation.ConversationScreen(
            state = ConversationUiState(),
            onIntent = {},
            onNavigateToLawyerProfile = {},
            onNavigateToBooking = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Conversation — With Messages")
@Composable
private fun ConversationWithMessagesPreview() {
    MizanTheme {
        ncshack.samba.mizan.presentation.ui.screens.conversation.ConversationScreen(
            state = ConversationUiState(
                items = listOf(
                    ConversationItem.UserMessage(id = "1", text = "What are my rights as a tenant?"),
                    ConversationItem.UserMessage(id = "2", text = "Can I break my lease early?"),
                ),
                inputText = "Tell me more about eviction laws",
            ),
            onIntent = {},
            onNavigateToLawyerProfile = {},
            onNavigateToBooking = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Conversation — Typing")
@Composable
private fun ConversationTypingPreview() {
    MizanTheme {
        ncshack.samba.mizan.presentation.ui.screens.conversation.ConversationScreen(
            state = ConversationUiState(
                items = listOf(
                    ConversationItem.UserMessage(id = "1", text = "What are my rights?"),
                ),
                isAwaitingResponse = true,
            ),
            onIntent = {},
            onNavigateToLawyerProfile = {},
            onNavigateToBooking = {},
        )
    }
}

// ──────────────────────────────────────────────
// MessageBubble
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "MessageBubble — User")
@Composable
private fun MessageBubbleUserPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            MessageBubble(text = "What are my rights as a tenant in Algeria?", isUser = true)
        }
    }
}

@Preview(showBackground = true, name = "MessageBubble — Assistant")
@Composable
private fun MessageBubbleAssistantPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            MessageBubble(
                text = "Under Algerian law, tenants have several protections including reasonable notice periods and protection against arbitrary eviction.",
                isUser = false,
            )
        }
    }
}

// ──────────────────────────────────────────────
// MessageInput
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "MessageInput — Empty")
@Composable
private fun MessageInputEmptyPreview() {
    MizanTheme {
        Surface {
            MessageInput(
                value = "",
                onValueChange = {},
                onSubmit = {},
                enabled = true,
            )
        }
    }
}

@Preview(showBackground = true, name = "MessageInput — With Text")
@Composable
private fun MessageInputTextPreview() {
    MizanTheme {
        Surface {
            MessageInput(
                value = "Can I break my lease early?",
                onValueChange = {},
                onSubmit = {},
                enabled = true,
            )
        }
    }
}

@Preview(showBackground = true, name = "MessageInput — Disabled")
@Composable
private fun MessageInputDisabledPreview() {
    MizanTheme {
        Surface {
            MessageInput(
                value = "Waiting for response...",
                onValueChange = {},
                onSubmit = {},
                enabled = false,
            )
        }
    }
}

// ──────────────────────────────────────────────
// TypingIndicator
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "TypingIndicator")
@Composable
private fun TypingIndicatorPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            TypingIndicator()
        }
    }
}

// ──────────────────────────────────────────────
// Cards
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "Card — ClarifyingChips")
@Composable
private fun ClarifyingChipsCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ClarifyingChipsCard(
                card = CardDescriptor(
                    id = "chip_1",
                    cardType = "CLARIFYING_CHIPS",
                    payload = """["Residential lease","Commercial lease","Shared housing","Sublease"]""",
                ),
                onChipSelected = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Context")
@Composable
private fun ContextCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ContextCard(
                card = CardDescriptor(
                    id = "ctx_1",
                    cardType = "CONTEXT",
                    payload = """{"title":"Tenant Rights in Algeria","content":"Article 17 of the Algerian Civil Code provides that tenants cannot be evicted without a court order. The landlord must give at least 3 months notice before termination of a residential lease.","citation":"Article 17, Algerian Civil Code (Ordonnance 75-58)"}""",
                ),
                onDismiss = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — LawyerCarousel")
@Composable
private fun LawyerCarouselCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LawyerCarouselCard(
                card = CardDescriptor(
                    id = "lawyer_1",
                    cardType = "LAWYER_CAROUSEL",
                    payload = """[{"id":"l1","fullName":"Me. Ahmed Benali","specialties":["Family Law","Civil Law"],"wilaya":"Alger","rating":4.8,"profilePictureUrl":""},{"id":"l2","fullName":"Me. Fatima Zohra Khelifi","specialties":["Criminal Law"],"wilaya":"Oran","rating":4.5,"profilePictureUrl":""},{"id":"l3","fullName":"Me. Youcef Amrani","specialties":["Business Law","Tax Law"],"wilaya":"Constantine","rating":4.9,"profilePictureUrl":""}]""",
                ),
                onNavigateToLawyerProfile = {},
                onNavigateToBooking = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Booking")
@Composable
private fun BookingCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            BookingCard(
                card = CardDescriptor(
                    id = "book_1",
                    cardType = "BOOKING",
                    payload = """{"slots":[{"id":"s1","label":"Mon 10:00 AM"},{"id":"s2","label":"Tue 2:00 PM"},{"id":"s3","label":"Wed 11:00 AM"}]}""",
                ),
                onBookingConfirmed = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — SubmitDocument")
@Composable
private fun SubmitDocumentCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            SubmitDocumentCard(
                card = CardDescriptor(
                    id = "doc_1",
                    cardType = "SUBMIT_DOCUMENT",
                    payload = """{"description":"Please upload your rental agreement for review.","sessionId":"sess_123"}""",
                ),
                onUploadTap = {},
                onPhotoTap = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — DownloadFile")
@Composable
private fun DownloadFileCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            DownloadFileCard(
                card = CardDescriptor(
                    id = "dl_1",
                    cardType = "DOWNLOAD_FILE",
                    payload = """{"fileName":"Tenant_Rights_Guide.pdf","description":"A comprehensive guide to tenant rights in Algeria.","fileUrl":"https://example.com/guide.pdf"}""",
                ),
                onDownload = {},
                onPreview = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Deadline")
@Composable
private fun DeadlineCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            DeadlineCard(
                card = CardDescriptor(
                    id = "deadline_1",
                    cardType = "DEADLINE",
                    payload = """{"label":"Lease renewal deadline","date":"2026-07-15","context":"Your current lease expires on this date. Review renewal terms.","deadlineId":"d_123"}""",
                ),
                onAddedToCalendar = {},
            )
        }
    }
}
