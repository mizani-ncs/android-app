package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor
import ncshack.samba.mizan.presentation.ui.components.expressiveListItemShape

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClarifyingChipsCard(
    card: CardDescriptor,
    onChipSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val payload = parseStepPayload(card.payload)
    var selected by remember { mutableStateOf<String?>(null) }

    SharedCard(
        title = payload.prompt.ifEmpty { "Options" },
        cardColor = CardColor.AMBER,
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            payload.options.forEachIndexed { index, option ->
                val isSelected = selected == option
                Surface(
                    shape = expressiveListItemShape(index, payload.options.size),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                    onClick = {
                        onChipSelected(option)
                    }
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            if (isSelected) Icons.Default.Check else Icons.AutoMirrored.Default.ArrowForward,
                            null,
                            Modifier.size(20.dp)
                        )
                        Text(
                            text = option,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
    }
}

private data class StepPayload(
    val prompt: String,
    val options: List<String>,
)

private fun parseStepPayload(payload: String): StepPayload = try {
    val json = Json.parseToJsonElement(payload).jsonObject
    StepPayload(
        prompt = json["prompt"]?.jsonPrimitive?.content ?: "",
        options = json["options"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList(),
    )
} catch (_: Exception) {
    StepPayload(prompt = "Options", options = emptyList())
}
