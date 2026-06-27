package ncshack.samba.mizan.data.remote

import android.util.Log
import com.apollographql.apollo.ApolloClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import ncshack.samba.mizan.WidgetPushSubscription
import ncshack.samba.mizan.domain.model.CardDescriptor

class WidgetPushDataSource(
    private val apolloClient: ApolloClient,
) {
    fun subscribe(sessionId: String): Flow<CardDescriptor?> =
        apolloClient.subscription(WidgetPushSubscription(sessionId = sessionId))
            .toFlow()
            .map { response ->
                Log.i("WidgetPushDataSource", "$response")
                val card = response.data?.widgetPush
                if (card == null) {
                    Log.w("WidgetPushDataSource", "Subscription response with null data: errors=${response.errors}, exception=${response.exception}")
                    return@map null
                }
                CardDescriptor.fromApolloPayload(
                    id = card.id,
                    cardType = card.cardType,
                    rawPayload = card.payload,
                )
            }
            .catch { e ->
                Log.e("WidgetPushDataSource", "Subscription flow error", e)
                emit(null)
            }
}
