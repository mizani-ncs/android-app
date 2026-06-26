package ncshack.samba.mizan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ncshack.samba.mizan.domain.usecase.CheckAuthStatusUseCase
import ncshack.samba.mizan.domain.usecase.LoginUseCase
import ncshack.samba.mizan.domain.usecase.RegisterUseCase
import ncshack.samba.mizan.domain.usecase.ValidationException

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val checkAuthStatusUseCase: CheckAuthStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AuthUiState(isAuthenticated = checkAuthStatusUseCase()),
    )
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _effect = Channel<AuthEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> _state.update {
                it.copy(email = event.email, errors = it.errors - "email")
            }
            is AuthEvent.PasswordChanged -> _state.update {
                it.copy(password = event.password, errors = it.errors - "password")
            }
            is AuthEvent.ConfirmPasswordChanged -> _state.update {
                it.copy(confirmPassword = event.confirmPassword, errors = it.errors - "confirmPassword")
            }
            AuthEvent.Login -> login()
            AuthEvent.Register -> register()
            AuthEvent.ClearErrors -> _state.update { it.copy(errors = emptyMap()) }
        }
    }

    private fun login() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errors = emptyMap()) }
            try {
                loginUseCase(s.email, s.password)
                _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                _effect.send(AuthEffect.NavigateToConversation)
            } catch (e: ValidationException) {
                _state.update { it.copy(isLoading = false, errors = e.errors) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.send(AuthEffect.ShowError(e.message ?: "Login failed"))
            }
        }
    }

    private fun register() {
        val s = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errors = emptyMap()) }
            try {
                registerUseCase(s.email, s.password, s.confirmPassword)
                _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                _effect.send(AuthEffect.NavigateToConversation)
            } catch (e: ValidationException) {
                _state.update { it.copy(isLoading = false, errors = e.errors) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.send(AuthEffect.ShowError(e.message ?: "Registration failed"))
            }
        }
    }
}
