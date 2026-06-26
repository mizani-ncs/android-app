package ncshack.samba.mizan.domain.usecase

import ncshack.samba.mizan.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): String {
        validate(email, password, confirmPassword)
        return authRepository.register(email, password, confirmPassword).getOrThrow()
    }

    private fun validate(email: String, password: String, confirmPassword: String) {
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
        if (confirmPassword.isBlank()) {
            errors["confirmPassword"] = "Please confirm your password"
        } else if (password != confirmPassword) {
            errors["confirmPassword"] = "Passwords do not match"
        }
        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}
