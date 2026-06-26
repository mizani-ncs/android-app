package ncshack.samba.mizan.domain.usecase

import ncshack.samba.mizan.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): String {
        validate(email, password)
        return authRepository.login(email, password).getOrThrow()
    }

    private fun validate(email: String, password: String) {
        val errors = mutableMapOf<String, String>()
        if (email.isBlank()) {
            errors["email"] = "Email is required"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors["email"] = "Invalid email"
        }
        if (password.isBlank()) {
            errors["password"] = "Password is required"
        } else if (password.length < 8) {
            errors["password"] = "Password must be at least 8 characters"
        }
        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}
