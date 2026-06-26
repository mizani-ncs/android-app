package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun DownloadFileCard(
    card: CardDescriptor,
    onDownload: () -> Unit,
    onPreview: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseDownloadPayload(card.payload)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        CardHeader(title = "Download File")

        CardBody {
            Text(
                text = payload.fileName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = payload.description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        CardFooter {
            OutlinedButton(
                onClick = onPreview,
                modifier = Modifier.weight(1f),
                shape = MaterialTheme.shapes.small,
            ) {
                Text("Preview")
            }
            Button(
                onClick = onDownload,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                shape = MaterialTheme.shapes.small,
            ) {
                Text("Download")
            }
        }
    }
}

private data class DownloadPayload(
    val fileName: String,
    val description: String,
    val fileUrl: String,
)

private fun parseDownloadPayload(payload: String): DownloadPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    DownloadPayload(
        fileName = json["fileName"]?.jsonPrimitive?.content ?: "Document",
        description = json["description"]?.jsonPrimitive?.content ?: "",
        fileUrl = json["fileUrl"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    DownloadPayload(fileName = "Document", description = "", fileUrl = "")
}
