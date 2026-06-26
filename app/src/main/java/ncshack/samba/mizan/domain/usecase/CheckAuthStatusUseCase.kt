package ncshack.samba.mizan.domain.usecase

import ncshack.samba.mizan.domain.repository.AuthRepository

class CheckAuthStatusUseCase(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(): Boolean = authRepository.isLoggedIn()
}
