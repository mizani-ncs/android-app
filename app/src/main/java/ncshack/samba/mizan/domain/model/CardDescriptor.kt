package ncshack.samba.mizan.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CardDescriptor(
    val id: String,
    val cardType: String,
    val payload: String,
)
