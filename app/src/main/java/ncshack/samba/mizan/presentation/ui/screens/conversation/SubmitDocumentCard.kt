package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
fun SubmitDocumentCard(
    card: CardDescriptor,
    onUploadTap: () -> Unit,
    onPhotoTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseDocumentPayload(card.payload)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        CardHeader(title = "Submit Document")

        CardBody {
            Text(
                text = payload.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        CardFooter {
            Button(
                onClick = onUploadTap,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text("Upload file")
            }
            Button(
                onClick = onPhotoTap,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text("Take photo")
            }
        }
    }
}

private data class DocumentPayload(
    val description: String,
    val sessionId: String,
)

private fun parseDocumentPayload(payload: String): DocumentPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    DocumentPayload(
        description = json["description"]?.jsonPrimitive?.content ?: "Upload your document",
        sessionId = json["sessionId"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    DocumentPayload(description = "Upload your document", sessionId = "")
}
