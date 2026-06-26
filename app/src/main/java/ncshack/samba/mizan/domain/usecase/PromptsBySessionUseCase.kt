package ncshack.samba.mizan.domain.usecase

import com.apollographql.apollo.ApolloClient
import ncshack.samba.mizan.PromptsBySessionQuery
import ncshack.samba.mizan.domain.model.CardDescriptor

data class PromptHistory(
    val id: String,
    val userText: String?,
    val cards: List<CardDescriptor>,
)

class PromptsBySessionUseCase(
    private val apolloClient: ApolloClient,
) {
    suspend operator fun invoke(sessionId: String): List<PromptHistory> {
        val response = apolloClient.query(
            PromptsBySessionQuery(sessionId = sessionId),
        ).execute()

        val errors = response.errors
        if (errors != null && errors.isNotEmpty()) {
            throw Exception(errors.first().message)
        }

        val prompts = response.data?.promptsBySession ?: return emptyList()
        return prompts.map { prompt ->
            val cards = prompt.responses
                ?.mapNotNull { it.cardPush }
                ?.map { card ->
                    CardDescriptor.fromApolloPayload(
                        id = card.id,
                        cardType = card.cardType,
                        rawPayload = card.payload,
                    )
                }
                ?: emptyList()

            PromptHistory(
                id = prompt.id,
                userText = prompt.content,
                cards = cards,
            )
        }
    }
}
