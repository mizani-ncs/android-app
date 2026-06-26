package ncshack.samba.mizan.data.remote

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import ncshack.samba.mizan.LoginMutation
import ncshack.samba.mizan.RegisterMutation
import ncshack.samba.mizan.domain.repository.AuthRepository
import ncshack.samba.mizan.type.LoginInput
import ncshack.samba.mizan.type.RegisterInput

class AuthRepositoryImpl(
    private val apolloClient: ApolloClient,
    private val context: Context,
) : AuthRepository {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val sharedPrefs = EncryptedSharedPreferences.create(
        "lexify_auth_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response: ApolloResponse<LoginMutation.Data> = apolloClient.mutation(
                LoginMutation(
                    loginInput = LoginInput(
                        email = email,
                        password = password,
                    ),
                ),
            ).execute()

            val data = response.data
            val errors = response.errors
            if (errors != null && errors.isNotEmpty()) {
                Result.failure(Exception(errors.first().message))
            } else if (data != null) {
                val token = data.login.accessToken.orEmpty()
                val userId = data.login.user.id
                saveToken(token)
                saveSessionId(userId)
                Result.success(token)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, confirmPassword: String): Result<String> {
        return try {
            val response = apolloClient.mutation(
                RegisterMutation(
                    registerInput = RegisterInput(
                        email = email,
                        password = password,
                        confirmPassword = confirmPassword,
                    ),
                ),
            ).execute()

            val data = response.data
            val errors = response.errors
            if (errors != null && errors.isNotEmpty()) {
                Result.failure(Exception(errors.first().message))
            } else if (data != null) {
                val token = data.register.accessToken.orEmpty()
                val userId = data.register.user.id
                saveToken(token)
                saveSessionId(userId)
                Result.success(token)
            } else {
                Result.failure(Exception("Registration failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        sharedPrefs.edit().clear().apply()
    }

    override fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    override fun getSessionId(): String? {
        return sharedPrefs.getString(KEY_SESSION_ID, null)
    }

    fun getToken(): String? {
        return sharedPrefs.getString(KEY_TOKEN, null)
    }

    private fun saveToken(token: String) {
        sharedPrefs.edit().putString(KEY_TOKEN, token).apply()
    }

    private fun saveSessionId(sessionId: String) {
        sharedPrefs.edit().putString(KEY_SESSION_ID, sessionId).apply()
    }

    companion object {
        private const val KEY_TOKEN = "jwt_token"
        private const val KEY_SESSION_ID = "session_id"
    }
}
