package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import ncshack.samba.mizan.domain.model.CardDescriptor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LawyerCarouselCard(
    card: CardDescriptor,
    onNavigateToLawyerProfile: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lawyers = parseLawyers(card.payload)
    LaunchedEffect(lawyers) {
        println("Lawyers: ${card.payload}, parsed: $lawyers")
    }
    SharedCard(
        title = "Recommended Lawyers",
        cardColor = CardColor.TEAL,
        modifier = modifier,
    ) {
        HorizontalCenteredHeroCarousel(
            state = rememberCarouselState {
                lawyers.size
            },
            itemSpacing = 8.dp,
            maxItemWidth = 260.dp
        ) { i ->
            val lawyer = lawyers[i]
            LawyerMiniCard(
                lawyer = lawyer,
                onProfileClick = { onNavigateToLawyerProfile(lawyer.id) },
                onBookClick = { onNavigateToBooking(lawyer.id) },
            )
        }
    }
}

@Composable
private fun CarouselItemScope.LawyerMiniCard(
    lawyer: LawyerData,
    onProfileClick: () -> Unit,
    onBookClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(rememberMaskShape(MaterialTheme.shapes.large))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        AsyncImage(
            model = lawyer.image,
            contentDescription = lawyer.name,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(4f / 3)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentScale = ContentScale.Crop,
        )
        Column(Modifier.fillMaxWidth().padding(12.dp).alpha(carouselItemDrawInfo.size / carouselItemDrawInfo.maxSize)) {
            Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lawyer.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis
                )
            Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = lawyer.specialty,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            Spacer(modifier = Modifier.height(8.dp))

            if (lawyer.rating > 0f) {
                Text(
                    text = "${lawyer.rating}★",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedIconButton(onClick = onProfileClick) {
                    Icon(Icons.Default.Person, null)
                }
                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    ),
                    modifier = Modifier.weight(1f)
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
    val rating: Float,
    val image: String?,
)

private fun parseLawyers(payload: String): List<LawyerData> = try {
    println("Lawyers payload: $payload")
    val json = Json.parseToJsonElement(payload).jsonObject
    println("Lawyers json: $json")
    val lawyersArray = json["lawyers"]?.jsonArray ?: return emptyList()
    lawyersArray.map { element ->
        val obj = element.jsonObject
        LawyerData(
            id = obj["id"]?.jsonPrimitive?.content ?: "",
            name = obj["name"]?.jsonPrimitive?.content ?: "",
            specialty = obj["specialty"]?.jsonPrimitive?.content ?: "",
            rating = obj["rating"]?.jsonPrimitive?.content?.toFloatOrNull() ?: 0f,
            image = obj["image"]?.let { if (it is JsonNull) null else it.jsonPrimitive.contentOrNull },
        )
    }
} catch (_: Exception) {
    emptyList()
}
