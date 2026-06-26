package ncshack.samba.mizan.presentation.viewmodel

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errors: Map<String, String> = emptyMap(),
) {
    val canLogin: Boolean
        get() = email.isNotBlank() && password.isNotBlank() && !isLoading

    val canRegister: Boolean
        get() = email.isNotBlank() &&
            password.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            !isLoading
}
