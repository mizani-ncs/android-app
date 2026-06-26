package ncshack.samba.mizan.presentation.viewmodel

sealed interface AuthEvent {
    data class EmailChanged(val email: String) : AuthEvent
    data class PasswordChanged(val password: String) : AuthEvent
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent
    data object Login : AuthEvent
    data object Register : AuthEvent
    data object ClearErrors : AuthEvent
}
