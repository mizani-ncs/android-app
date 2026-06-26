package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@Composable
fun LawyerCarouselCard(
    card: CardDescriptor,
    onNavigateToLawyerProfile: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lawyers = parseLawyers(card.payload)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        CardHeader(title = "Recommended Lawyers")

        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(lawyers) { lawyer ->
                LawyerMiniCard(
                    lawyer = lawyer,
                    onProfileClick = { onNavigateToLawyerProfile(lawyer.id) },
                    onBookClick = { onNavigateToBooking(lawyer.id) },
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun LawyerMiniCard(
    lawyer: LawyerData,
    onProfileClick: () -> Unit,
    onBookClick: () -> Unit,
) {
    Card(
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = lawyer.avatarUrl,
                    contentDescription = lawyer.name,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = lawyer.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = lawyer.specialty,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${lawyer.wilaya} • ${lawyer.rating}★",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onProfileClick) {
                    Text("View profile")
                }
                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text("Book now")
                }
            }
        }
    }
}

private data class LawyerData(
    val id: String,
    val name: String,
    val specialty: String,
    val wilaya: String,
    val rating: Float,
    val avatarUrl: String,
)

private fun parseLawyers(payload: String): List<LawyerData> = try {
    val json = Json.parseToJsonElement(payload).jsonArray
    json.map { element ->
        val obj = element.jsonObject
        val specialties = obj["specialties"]?.jsonArray?.map { it.jsonPrimitive.content } ?: emptyList()
        LawyerData(
            id = obj["id"]?.jsonPrimitive?.content ?: "",
            name = obj["fullName"]?.jsonPrimitive?.content ?: "",
            specialty = specialties.joinToString(", "),
            wilaya = obj["wilaya"]?.jsonPrimitive?.content ?: "",
            rating = obj["rating"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0f,
            avatarUrl = obj["profilePictureUrl"]?.jsonPrimitive?.content ?: "",
        )
    }
} catch (_: Exception) {
    emptyList()
}
