package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun CardRenderer(
    card: CardDescriptor,
    onActionTapped: () -> Unit,
    onNavigateToLawyerProfile: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (card.cardType) {
        "text" -> MessageBubble(
            text = extractTextPayload(card.payload),
            isUser = false,
            modifier = modifier,
        )
        "info" -> InfoCard(
            card = card,
            modifier = modifier,
        )
        "lawyer_carousel" -> LawyerCarouselCard(
            card = card,
            onNavigateToLawyerProfile = onNavigateToLawyerProfile,
            onNavigateToBooking = onNavigateToBooking,
            modifier = modifier,
        )
        "submit_document" -> SubmitDocumentCard(
            card = card,
            onUploadTap = onActionTapped,
            modifier = modifier,
        )
        "download_file" -> DownloadFileCard(
            card = card,
            onDownload = onActionTapped,
            modifier = modifier,
        )
        "deadline" -> DeadlineCard(
            card = card,
            onAddedToCalendar = onActionTapped,
            modifier = modifier,
        )
        "context" -> ContextCard(
            card = card,
            onDismiss = onActionTapped,
            modifier = modifier,
        )
        "step" -> ClarifyingChipsCard(
            card = card,
            onChipSelected = { onActionTapped() },
            modifier = modifier,
        )
        "legal_analysis" -> LegalAnalysisCard(
            card = card,
            modifier = modifier,
        )
        "knowledge_result" -> KnowledgeResultCard(
            card = card,
            modifier = modifier,
        )
        "action_required" -> ActionRequiredCard(
            card = card,
            onActionTapped = onActionTapped,
            modifier = modifier,
        )
        "booking" -> BookingCard(
            card = card,
            onBookingConfirmed = { onNavigateToBooking(it) },
            modifier = modifier,
        )
    }
}

private fun extractTextPayload(payload: String): String = try {
    val json = kotlinx.serialization.json.Json.parseToJsonElement(payload)
    json.jsonObject["text"]?.jsonPrimitive?.content ?: payload
} catch (_: Exception) {
    payload
}
