package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        "LAWYER_CAROUSEL" -> LawyerCarouselCard(
            card = card,
            onNavigateToLawyerProfile = onNavigateToLawyerProfile,
            onNavigateToBooking = onNavigateToBooking,
            modifier = modifier,
        )
        "SUBMIT_DOCUMENT" -> SubmitDocumentCard(
            card = card,
            onUploadTap = onActionTapped,
            onPhotoTap = onActionTapped,
            modifier = modifier,
        )
        "DOWNLOAD_FILE" -> DownloadFileCard(
            card = card,
            onDownload = onActionTapped,
            onPreview = onActionTapped,
            modifier = modifier,
        )
        "DEADLINE" -> DeadlineCard(
            card = card,
            onAddedToCalendar = onActionTapped,
            modifier = modifier,
        )
        "CONTEXT" -> ContextCard(
            card = card,
            onDismiss = onActionTapped,
            modifier = modifier,
        )
        "CLARIFYING_CHIPS" -> ClarifyingChipsCard(
            card = card,
            onChipSelected = { onActionTapped() },
            modifier = modifier,
        )
        "BOOKING" -> BookingCard(
            card = card,
            onBookingConfirmed = { onNavigateToBooking(it) },
            modifier = modifier,
        )
    }
}
