package ncshack.samba.mizan.domain.model

data class LocalDocument(
    val id: String,
    val name: String,
    val downloadedAt: Long,
    val localUri: String,
    val type: String
)