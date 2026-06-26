package ncshack.samba.mizan.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.LoggingInterceptor
import io.ktor.client.HttpClient
import ncshack.samba.mizan.data.remote.ApiService
import ncshack.samba.mizan.data.remote.AuthRepositoryImpl
import ncshack.samba.mizan.data.remote.FileApiService
import ncshack.samba.mizan.data.remote.WidgetPushDataSource
import ncshack.samba.mizan.data.remote.provideKtorClient
import ncshack.samba.mizan.domain.repository.AuthRepository
import ncshack.samba.mizan.domain.usecase.CheckAuthStatusUseCase
import ncshack.samba.mizan.domain.usecase.LoginUseCase
import ncshack.samba.mizan.domain.usecase.PromptUseCase
import ncshack.samba.mizan.domain.usecase.RegisterUseCase
import ncshack.samba.mizan.presentation.viewmodel.AuthViewModel
import ncshack.samba.mizan.presentation.viewmodel.ConversationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl(ApiService.GRAPHQL_URL)
            .addHttpInterceptor(LoggingInterceptor())
            .build()
    }

    single<HttpClient> { provideKtorClient() }

    single<AuthRepository> {
        AuthRepositoryImpl(
            apolloClient = get(),
            context = androidContext(),
        )
    }

    singleOf(::WidgetPushDataSource)
    singleOf(::FileApiService)

    factoryOf(::CheckAuthStatusUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::PromptUseCase)

    viewModelOf(::AuthViewModel)
    viewModelOf(::ConversationViewModel)
}
