package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
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

    SharedCard(
        title = "Context",
        cardColor = CardColor.PURPLE,
        badge = "Legal Context",
        modifier = modifier,
    ) {
        Text(
            text = payload.text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = payload.disclaimer,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}

private data class ContextPayload(
    val text: String,
    val disclaimer: String,
    val language: String,
)

private fun parseContextPayload(payload: String): ContextPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    ContextPayload(
        text = json["text"]?.jsonPrimitive?.content ?: "",
        disclaimer = json["disclaimer"]?.jsonPrimitive?.content ?: "Ceci n'est pas un avis juridique.",
        language = json["language"]?.jsonPrimitive?.content ?: "fr",
    )
} catch (_: Exception) {
    ContextPayload(text = payload, disclaimer = "Ceci n'est pas un avis juridique.", language = "fr")
}
