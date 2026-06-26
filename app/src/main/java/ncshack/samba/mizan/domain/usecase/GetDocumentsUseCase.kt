package ncshack.samba.mizan.domain.usecase

import kotlinx.coroutines.flow.Flow
import ncshack.samba.mizan.domain.model.LocalDocument
import ncshack.samba.mizan.domain.repository.DocumentRepository

class GetDocumentsUseCase(private val repository: DocumentRepository) {
    operator fun invoke(): Flow<List<LocalDocument>> {
        return repository.getAllDocuments()
    }
}