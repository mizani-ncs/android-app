package ncshack.samba.mizan.domain.usecase

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import ncshack.samba.mizan.CreatePromptMutation
import ncshack.samba.mizan.type.CreatePromptInput

class PromptUseCase(
    private val apolloClient: ApolloClient,
) {
    suspend operator fun invoke(sessionId: String, text: String): Boolean {
        val response = apolloClient.mutation(
            CreatePromptMutation(
                input = CreatePromptInput(
                    sessionId = sessionId,
                    content = Optional.present(text),
                ),
            ),
        ).execute()

        val errors = response.errors
        if (errors != null && errors.isNotEmpty()) {
            throw Exception(errors.first().message)
        }
        return response.data?.createPrompt != null
    }
}
