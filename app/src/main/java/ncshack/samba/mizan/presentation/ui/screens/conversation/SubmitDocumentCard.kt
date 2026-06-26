package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun SubmitDocumentCard(
    card: CardDescriptor,
    onUploadTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseDocumentPayload(card.payload)
    val color = MaterialTheme.colorScheme.primary
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

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            onClick = onUploadTap,
            modifier = Modifier.fillMaxWidth().drawBehind {
                drawRoundRect(
                    color = color,
                    style = Stroke(width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    ),
                    cornerRadius = CornerRadius(packedValue = 24.dp.roundToPx().toLong())
                )
            },
            contentColor = MaterialTheme.colorScheme.primary,
            shape = MaterialTheme.shapes.large,

        ) {
            Column(
                Modifier.fillMaxSize().padding(vertical = 40.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    onClick = onUploadTap,
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.FileUpload, null, Modifier.size(30.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Upload file")
            }
        }
    }
}

private data class DocumentPayload(
    val title: String,
    val description: String,
    val acceptedTypes: List<String>,
)

private fun parseDocumentPayload(payload: String): DocumentPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    val acceptedTypes =
        json["acceptedTypes"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
    DocumentPayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Submit Document",
        description = json["description"]?.jsonPrimitive?.content ?: "Upload your document",
        acceptedTypes = acceptedTypes,
    )
} catch (_: Exception) {
    DocumentPayload("Submit Document", "Upload your document", emptyList())
}
