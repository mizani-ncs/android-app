package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.domain.model.CardDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun InfoCard(
    card: CardDescriptor,
    modifier: Modifier = Modifier,
) {
    val payload = parseInfoPayload(card.payload)

    SharedCard(
        title = payload.title,
        cardColor = CardColor.BLUE,
        badge = "Info",
        modifier = modifier,
    ) {
        Text(
            text = payload.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (payload.source?.isNotEmpty() == true) {
            TextButton({}, modifier = Modifier.padding(top = 8.dp)) {
                Icon(Icons.Default.Link, null, Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = payload.source,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

private data class InfoPayload(
    val title: String,
    val source: String?,
    val text: String,
)

private fun parseInfoPayload(payload: String): InfoPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    InfoPayload(
        title = json["title"]?.jsonPrimitive?.content ?: "",
        source = json["source"]?.let { if (it is JsonNull) null else it.jsonPrimitive.contentOrNull },
        text = json["text"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    InfoPayload("", null, "")
}
