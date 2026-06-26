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
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun ActionRequiredCard(
    card: CardDescriptor,
    onActionTapped: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseActionPayload(card.payload)

    SharedCard(
        title = payload.title,
        cardColor = CardColor.ORANGE,
        badge = "Action",
        modifier = modifier,
    ) {
        Text(
            text = payload.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))

        payload.actions.forEach { action ->
            Button(
                onClick = onActionTapped,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                Text(action.label)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private data class ActionItem(
    val label: String,
    val cardType: String,
)

private data class ActionPayload(
    val title: String,
    val description: String,
    val actions: List<ActionItem>,
)

private fun parseActionPayload(payload: String): ActionPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    val actions = json["actions"]?.jsonArray?.map { el ->
        val obj = el.jsonObject
        ActionItem(
            label = obj["label"]?.jsonPrimitive?.content ?: "",
            cardType = obj["cardType"]?.jsonPrimitive?.content ?: "",
        )
    } ?: emptyList()
    ActionPayload(
        title = json["title"]?.jsonPrimitive?.content ?: "Action Required",
        description = json["description"]?.jsonPrimitive?.content ?: "",
        actions = actions,
    )
} catch (_: Exception) {
    ActionPayload("Action Required", payload, emptyList())
}
