package ncshack.samba.mizan.data.remote

import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ncshack.samba.mizan.WidgetPushSubscription
import ncshack.samba.mizan.domain.model.CardDescriptor

class WidgetPushDataSource(
    private val apolloClient: ApolloClient,
) {
    fun subscribe(sessionId: String): Flow<CardDescriptor> =
        apolloClient.subscription(WidgetPushSubscription(sessionId = sessionId))
            .toFlow()
            .map { response ->
                val card = response.data?.widgetPush
                CardDescriptor(
                    id = card?.id.orEmpty(),
                    cardType = card?.cardType.orEmpty(),
                    payload = card?.payload.toString(),
                )
            }
}
