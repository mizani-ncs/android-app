package ncshack.samba.mizan.domain.usecase

import ncshack.samba.mizan.domain.model.Session
import ncshack.samba.mizan.domain.repository.SessionRepository

class StartSessionUseCase(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(language: String = "fr"): Session {
        return sessionRepository.startSession(language).getOrThrow()
    }
}
