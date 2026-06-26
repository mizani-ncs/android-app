package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor
import ncshack.samba.mizan.presentation.ui.components.expressiveListItemShape

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingCard(
    card: CardDescriptor,
    onBookingConfirmed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseBookingPayload(card.payload)
    var selectedSlot by remember { mutableStateOf<String?>(null) }
    var isBooked by remember { mutableStateOf(false) }

    SharedCard(
        title = "Booking — ${payload.lawyerName}",
        cardColor = CardColor.CYAN,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            payload.slots.forEachIndexed { index, option ->
                val isSelected = selectedSlot == option
                Surface(
                    shape = expressiveListItemShape(index, payload.slots.size),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerLow
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            if (isSelected) Icons.Default.Check else Icons.AutoMirrored.Default.ArrowForward,
                            null,
                            Modifier.size(20.dp)
                        )
                        Text(
                            text = option,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isBooked) {
            Button(
                onClick = {
                    selectedSlot?.let { onBookingConfirmed(it) }
                    isBooked = true
                },
                enabled = selectedSlot != null,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text("Confirm booking")
            }
        } else {
            Text(
                text = "Booking confirmed!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private data class BookingPayload(
    val lawyerId: String,
    val lawyerName: String,
    val slots: List<String>,
)

private fun parseBookingPayload(payload: String): BookingPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    val slots = json["slots"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    BookingPayload(
        lawyerId = json["lawyerId"]?.jsonPrimitive?.content ?: "",
        lawyerName = json["lawyerName"]?.jsonPrimitive?.content ?: "",
        slots = slots,
    )
} catch (_: Exception) {
    BookingPayload("", "", emptyList())
}
