package ncshack.samba.mizan.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
    suspend fun register(email: String, password: String, confirmPassword: String): Result<String>
    fun logout()
    fun isLoggedIn(): Boolean
    fun getSessionId(): String?
}
