package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingCard(
    card: CardDescriptor,
    onBookingConfirmed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val slots = parseBookingSlots(card.payload)
    var selectedSlot by remember { mutableStateOf<BookingSlotData?>(null) }
    var isBooked by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        CardHeader(title = "Available Time Slots")

        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            slots.forEach { slot ->
                FilterChip(
                    selected = selectedSlot?.id == slot.id,
                    onClick = { selectedSlot = slot },
                    label = {
                        Text(slot.label)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (!isBooked) {
            Button(
                onClick = {
                    selectedSlot?.let { onBookingConfirmed(it.id) }
                    isBooked = true
                },
                enabled = selectedSlot != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text("Confirm booking")
            }
        } else {
            Text(
                text = "Booking confirmed!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

private data class BookingSlotData(
    val id: String,
    val label: String,
)

private fun parseBookingSlots(payload: String): List<BookingSlotData> = try {
    val json = Json.parseToJsonElement(payload)
    val slotsArray = when {
        json is JsonArray -> json.jsonArray
        json.jsonObject.containsKey("slots") -> json.jsonObject["slots"]?.jsonArray ?: emptyList()
        else -> emptyList()
    }
    slotsArray.map { element ->
        val obj = element.jsonObject
        val id = obj["id"]?.jsonPrimitive?.content ?: ""
        val label = obj["label"]?.jsonPrimitive?.content
            ?: obj["time"]?.jsonPrimitive?.content
            ?: obj["meetingAt"]?.jsonPrimitive?.content
            ?: id
        BookingSlotData(id = id, label = label)
    }
} catch (_: Exception) {
    emptyList()
}
