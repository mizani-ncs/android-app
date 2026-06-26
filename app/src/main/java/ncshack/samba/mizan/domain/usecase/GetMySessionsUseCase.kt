package ncshack.samba.mizan.domain.usecase

import ncshack.samba.mizan.domain.model.Session
import ncshack.samba.mizan.domain.repository.SessionRepository

class GetMySessionsUseCase(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(): List<Session> {
        return sessionRepository.getMySessions().getOrDefault(emptyList())
    }
}
