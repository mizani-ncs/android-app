package ncshack.samba.mizan.domain.model

import kotlinx.serialization.Serializable
import org.json.JSONObject

@Serializable
data class CardDescriptor(
    val id: String,
    val cardType: String,
    val payload: String,
) {
    companion object {
        fun fromApolloPayload(id: String, cardType: String, rawPayload: Any?): CardDescriptor =
            CardDescriptor(
                id = id,
                cardType = cardType,
                payload = when (rawPayload) {
                    is Map<*, *> -> JSONObject(
                        rawPayload.mapKeys { it.key.toString() }
                            .mapValues { it.value ?: JSONObject.NULL },
                    ).toString()
                    is String -> rawPayload
                    else -> rawPayload?.toString().orEmpty()
                },
            )
    }
}
