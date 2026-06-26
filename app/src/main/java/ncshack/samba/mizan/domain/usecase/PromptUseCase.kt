package ncshack.samba.mizan.domain.usecase

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import ncshack.samba.mizan.CreatePromptMutation
import ncshack.samba.mizan.domain.model.CardDescriptor
import ncshack.samba.mizan.type.CreatePromptInput

class PromptUseCase(
    private val apolloClient: ApolloClient,
) {
    suspend operator fun invoke(sessionId: String, text: String): List<CardDescriptor> {
        val response = apolloClient.mutation(
            CreatePromptMutation(
                input = CreatePromptInput(
                    sessionId = sessionId,
                    content = Optional.present(text),
                ),
            ),
        ).execute()

        val errors = response.errors
        if (!errors.isNullOrEmpty()) {
            throw Exception(errors.first().message)
        }

        val prompt = response.data?.createPrompt ?: return emptyList()
        return prompt.responses
            .mapNotNull { it.cardPush }
            .map { card ->
                CardDescriptor.fromApolloPayload(
                    id = card.id,
                    cardType = card.cardType,
                    rawPayload = card.payload,
                )
            }
            ?: emptyList()
    }
}
