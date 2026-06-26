package ncshack.samba.mizan.domain.repository

import ncshack.samba.mizan.domain.model.Session

interface SessionRepository {
    suspend fun startSession(language: String = "fr"): Result<Session>
    suspend fun getMySessions(): Result<List<Session>>
}
