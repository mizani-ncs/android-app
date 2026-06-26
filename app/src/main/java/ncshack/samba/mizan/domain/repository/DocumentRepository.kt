package ncshack.samba.mizan.domain.repository

import kotlinx.coroutines.flow.Flow
import ncshack.samba.mizan.domain.model.LocalDocument

interface DocumentRepository {
    fun getAllDocuments(): Flow<List<LocalDocument>>
    suspend fun saveDocument(document: LocalDocument)
    suspend fun deleteDocument(id: String)
}