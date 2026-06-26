package ncshack.samba.mizan.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.http.LoggingInterceptor
import io.ktor.client.HttpClient
import ncshack.samba.mizan.data.remote.ApiService
import ncshack.samba.mizan.data.remote.AuthInterceptor
import ncshack.samba.mizan.data.remote.AuthRepositoryImpl
import ncshack.samba.mizan.data.remote.FileApiService
import ncshack.samba.mizan.data.remote.SessionRepositoryImpl
import ncshack.samba.mizan.data.remote.WidgetPushDataSource
import ncshack.samba.mizan.data.remote.provideKtorClient
import ncshack.samba.mizan.domain.repository.AuthRepository
import ncshack.samba.mizan.domain.repository.SessionRepository
import ncshack.samba.mizan.domain.usecase.CheckAuthStatusUseCase
import ncshack.samba.mizan.domain.usecase.GetMySessionsUseCase
import ncshack.samba.mizan.domain.usecase.LoginUseCase
import ncshack.samba.mizan.domain.usecase.PromptUseCase
import ncshack.samba.mizan.domain.usecase.PromptsBySessionUseCase
import ncshack.samba.mizan.domain.usecase.RegisterUseCase
import ncshack.samba.mizan.domain.usecase.StartSessionUseCase
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
            .addHttpInterceptor(AuthInterceptor(androidContext()))
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

    single<SessionRepository> {
        SessionRepositoryImpl(apolloClient = get())
    }

    singleOf(::WidgetPushDataSource)
    singleOf(::FileApiService)

    factoryOf(::CheckAuthStatusUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::PromptUseCase)
    factoryOf(::PromptsBySessionUseCase)
    factoryOf(::StartSessionUseCase)
    factoryOf(::GetMySessionsUseCase)

    viewModelOf(::AuthViewModel)
    viewModelOf(::ConversationViewModel)
}
