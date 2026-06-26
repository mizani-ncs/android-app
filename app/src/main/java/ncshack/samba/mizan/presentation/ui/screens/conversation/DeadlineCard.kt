package ncshack.samba.mizan.presentation.ui.screens.conversation

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun DeadlineCard(
    card: CardDescriptor,
    onAddedToCalendar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseDeadlinePayload(card.payload)
    val context = LocalContext.current
    var isAdded by remember { mutableStateOf(false) }

    SharedCard(
        title = "Deadline",
        cardColor = CardColor.RED,
        modifier = modifier,
    ) {
        Text(
            text = payload.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = payload.date,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (!isAdded) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Events.CONTENT_URI
                        putExtra(CalendarContract.Events.TITLE, payload.title)
                    }
                    context.startActivity(intent)
                    isAdded = true
                    onAddedToCalendar()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text("Add to calendar")
            }
        } else {
            Text(
                text = "Added to calendar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

private data class DeadlinePayload(
    val title: String,
    val date: String,
)

private fun parseDeadlinePayload(payload: String): DeadlinePayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    DeadlinePayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Deadline",
        date = json["date"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    DeadlinePayload(title = "Deadline", date = "")
}
