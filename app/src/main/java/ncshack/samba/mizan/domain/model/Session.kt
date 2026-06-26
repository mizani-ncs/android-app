package ncshack.samba.mizan.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: String,
    val channel: String,
    val language: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
)
