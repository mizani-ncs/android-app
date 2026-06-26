package ncshack.samba.mizan.presentation.ui.screens.conversation

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DeadlineCard(
    card: CardDescriptor,
    onAddedToCalendar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseDeadlinePayload(card.payload)
    val context = LocalContext.current
    var isAdded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        CardHeader(title = "Deadline")

        CardBody {
            Text(
                text = payload.label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = payload.date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (payload.context.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = payload.context,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!isAdded) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Events.CONTENT_URI
                        putExtra(CalendarContract.Events.TITLE, payload.label)
                        putExtra(CalendarContract.Events.DESCRIPTION, payload.context)
                    }
                    context.startActivity(intent)
                    isAdded = true
                    onAddedToCalendar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text("Add to calendar")
            }
        } else {
            Text(
                text = "Added to calendar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

private data class DeadlinePayload(
    val label: String,
    val date: String,
    val context: String,
    val deadlineId: String,
)

private fun parseDeadlinePayload(payload: String): DeadlinePayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    DeadlinePayload(
        label = json["label"]?.jsonPrimitive?.content ?: "Deadline",
        date = json["date"]?.jsonPrimitive?.content ?: "",
        context = json["context"]?.jsonPrimitive?.content ?: "",
        deadlineId = json["deadlineId"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    DeadlinePayload(label = "Deadline", date = "", context = "", deadlineId = "")
}
