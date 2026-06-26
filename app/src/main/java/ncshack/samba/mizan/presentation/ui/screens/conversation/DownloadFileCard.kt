package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun DownloadFileCard(
    card: CardDescriptor,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseDownloadPayload(card.payload)

    SharedCard(
        title = payload.title,
        cardColor = CardColor.INDIGO,
        modifier = modifier,
    ) {
        Text(
            text = payload.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onDownload,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            Text("Download")
        }
    }
}

private data class DownloadPayload(
    val title: String,
    val description: String,
    val downloadUrl: String,
)

private fun parseDownloadPayload(payload: String): DownloadPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    DownloadPayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Download File",
        description = json["description"]?.jsonPrimitive?.content ?: "",
        downloadUrl = json["downloadUrl"]?.jsonPrimitive?.content ?: "",
    )
} catch (_: Exception) {
    DownloadPayload("Download File", "", "")
}
