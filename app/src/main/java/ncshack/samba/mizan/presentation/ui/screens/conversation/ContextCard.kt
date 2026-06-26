package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun ContextCard(
    card: CardDescriptor,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseContextPayload(card.payload)

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
    ) {
        CardHeader(
            title = payload.title,
            badge = "Legal Context",
        )

        CardBody {
            Text(
                text = payload.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (payload.citation.isNotEmpty()) {
                Text(
                    text = payload.citation,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This information is for guidance only and does not constitute legal advice.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private data class ContextPayload(
    val title: String,
    val content: String,
    val citation: String,
)

private fun parseContextPayload(payload: String): ContextPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    ContextPayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Context",
        content = json["content"]?.jsonPrimitive?.content ?: "",
        citation = json["citation"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    ContextPayload(title = "Context", content = payload, citation = "")
}
