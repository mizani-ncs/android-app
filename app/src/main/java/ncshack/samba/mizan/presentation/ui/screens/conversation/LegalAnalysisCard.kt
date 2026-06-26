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

@Composable
fun LegalAnalysisCard(
    card: CardDescriptor,
    modifier: Modifier = Modifier,
) {
    val payload = parseLegalAnalysisPayload(card.payload)

    SharedCard(
        title = payload.title,
        cardColor = CardColor.DEEP_PURPLE,
        badge = "Legal Analysis",
        modifier = modifier,
    ) {
        payload.sections.forEach { section ->
            Text(
                text = section.heading,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = section.body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
        }

        if (payload.citations.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = "Sources",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            payload.citations.forEach { citation ->
                Text(
                    text = buildString {
                        append(citation.lawId)
                        if (citation.article.isNotEmpty()) append(" — Art. ${citation.article}")
                        append(": ${citation.text}")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider()

        Text(
            text = payload.disclaimer,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

private data class AnalysisSection(
    val heading: String,
    val body: String,
)

private data class AnalysisCitation(
    val lawId: String,
    val article: String,
    val text: String,
)

private data class LegalAnalysisPayload(
    val title: String,
    val sections: List<AnalysisSection>,
    val citations: List<AnalysisCitation>,
    val disclaimer: String,
)

private fun parseLegalAnalysisPayload(payload: String): LegalAnalysisPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    val sections = json["sections"]?.jsonArray?.map { el ->
        val obj = el.jsonObject
        AnalysisSection(
            heading = obj["heading"]?.jsonPrimitive?.content ?: "",
            body = obj["body"]?.jsonPrimitive?.content ?: "",
        )
    } ?: emptyList()
    val citations = json["citations"]?.jsonArray?.map { el ->
        val obj = el.jsonObject
        AnalysisCitation(
            lawId = obj["lawId"]?.jsonPrimitive?.content ?: "",
            article = obj["article"]?.jsonPrimitive?.content ?: "",
            text = obj["text"]?.jsonPrimitive?.content ?: "",
        )
    } ?: emptyList()
    LegalAnalysisPayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Legal Analysis",
        sections = sections,
        citations = citations,
        disclaimer = json["disclaimer"]?.jsonPrimitive?.content ?: "Ceci n'est pas un avis juridique.",
    )
} catch (_: Exception) {
    LegalAnalysisPayload("Legal Analysis", emptyList(), emptyList(), "Ceci n'est pas un avis juridique.")
}
