package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun CardGroup(
    cards: List<CardDescriptor>,
    onCardActionTapped: (String) -> Unit,
    onNavigateToLawyerProfile: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        cards.forEach { card ->
            CardRenderer(
                card = card,
                onActionTapped = { onCardActionTapped(card.id) },
                onNavigateToLawyerProfile = onNavigateToLawyerProfile,
                onNavigateToBooking = onNavigateToBooking,
            )
        }
    }
}
