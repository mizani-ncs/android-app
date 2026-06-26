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
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor
import kotlin.math.roundToInt

@Composable
fun KnowledgeResultCard(
    card: CardDescriptor,
    modifier: Modifier = Modifier,
) {
    val payload = parseKnowledgePayload(card.payload)

    SharedCard(
        title = payload.title,
        cardColor = CardColor.GREEN,
        badge = "Knowledge",
        modifier = modifier,
    ) {
        Text(
            text = payload.answer,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (payload.confidence > 0f) {
            Text(
                text = "Confidence: ${(payload.confidence * 100).roundToInt()}%",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (payload.sources.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Text(
                text = "Sources",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            payload.sources.forEach { source ->
                Text(
                    text = source.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp),
                )
                if (source.snippet.isNotEmpty()) {
                    Text(
                        text = source.snippet,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

private data class KnowledgeSource(
    val title: String,
    val url: String,
    val snippet: String,
)

private data class KnowledgePayload(
    val title: String,
    val answer: String,
    val sources: List<KnowledgeSource>,
    val confidence: Float,
)

private fun parseKnowledgePayload(payload: String): KnowledgePayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    val sources = json["sources"]?.jsonArray?.map { el ->
        val obj = el.jsonObject
        KnowledgeSource(
            title = obj["title"]?.jsonPrimitive?.content ?: "",
            url = obj["url"]?.jsonPrimitive?.content ?: "",
            snippet = obj["snippet"]?.jsonPrimitive?.content ?: "",
        )
    } ?: emptyList()
    KnowledgePayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Results",
        answer = json["answer"]?.jsonPrimitive?.content ?: "",
        sources = sources,
        confidence = json["confidence"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0f,
    )
} catch (_: Exception) {
    KnowledgePayload("Results", payload, emptyList(), 0f)
}
