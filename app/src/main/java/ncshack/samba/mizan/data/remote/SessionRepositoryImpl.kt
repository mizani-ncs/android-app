package ncshack.samba.mizan.data.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import ncshack.samba.mizan.MySessionsQuery
import ncshack.samba.mizan.StartSessionMutation
import ncshack.samba.mizan.domain.model.Session
import ncshack.samba.mizan.domain.repository.SessionRepository
import ncshack.samba.mizan.type.StartSessionInput

class SessionRepositoryImpl(
    private val apolloClient: ApolloClient,
) : SessionRepository {

    override suspend fun startSession(language: String): Result<Session> {
        return try {
            val response = apolloClient.mutation(
                StartSessionMutation(
                    input = Optional.present(StartSessionInput(language = Optional.present(language))),
                ),
            ).execute()

            val data = response.data
            val errors = response.errors
            if (!errors.isNullOrEmpty()) {
                Result.failure(Exception(errors.first().message))
            } else if (data != null) {
                val session = data.startSession
                Result.success(
                    Session(
                        id = session.id,
                        channel = session.channel,
                        language = session.language,
                        status = session.status,
                        createdAt = session.createdAt.toString(),
                        updatedAt = session.createdAt.toString(),
                    ),
                )
            } else {
                Result.failure(Exception("Failed to start session"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMySessions(): Result<List<Session>> {
        return try {
            val response = apolloClient.query(MySessionsQuery()).execute()

            val data = response.data
            val errors = response.errors
            if (!errors.isNullOrEmpty()) {
                Result.failure(Exception(errors.first().message))
            } else if (data != null) {
                val sessions = data.mySessions.map { s ->
                    Session(
                        id = s.id,
                        channel = s.channel,
                        language = s.language,
                        status = s.status,
                        createdAt = s.createdAt.toString(),
                        updatedAt = s.updatedAt.toString(),
                    )
                }
                Result.success(sessions)
            } else {
                Result.failure(Exception("Failed to load sessions"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
