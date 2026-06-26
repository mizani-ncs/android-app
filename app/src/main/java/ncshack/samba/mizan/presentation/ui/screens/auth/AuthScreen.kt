package ncshack.samba.mizan.presentation.ui.screens.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.R
import ncshack.samba.mizan.presentation.ui.components.AuthTextField
import ncshack.samba.mizan.presentation.viewmodel.AuthEvent
import ncshack.samba.mizan.presentation.viewmodel.AuthUiState
import ncshack.samba.mizan.ui.theme.Dimens
import ncshack.samba.mizan.ui.theme.Primary

@Composable
fun AuthScreen(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    isLogin: Boolean,
    onToggleMode: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.systemBars),
    ) { _ ->
        Box(Modifier.fillMaxSize()) {
            HeaderBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .imePadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                HeaderContent(isLogin = isLogin)

                Spacer(modifier = Modifier.height(50.dp))

                AuthFormCard(
                    state = state,
                    onEvent = onEvent,
                    isLogin = isLogin,
                    focusManager = focusManager,
                )

                Spacer(modifier = Modifier.height(24.dp))

                AuthActionButton(
                    state = state,
                    isLogin = isLogin,
                    onEvent = onEvent,
                    focusManager = focusManager,
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthToggleText(
                    isLogin = isLogin,
                    onToggleMode = onToggleMode,
                )
            }
        }
    }
}

@Composable
private fun HeaderBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .background(Primary),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.star),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Composable
private fun HeaderContent(isLogin: Boolean) {
    Spacer(Modifier.height(40.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
        )
        Spacer(modifier = Modifier.height(12.dp))
        AnimatedContent(
            targetState = isLogin,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "header_title",
        ) { login ->
            Text(
                text = stringResource(
                    if (login) R.string.login_title else R.string.register_title,
                ),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(.8f),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent(
            targetState = isLogin,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "header_subtitle",
        ) { login ->
            Text(
                text = stringResource(
                    if (login) R.string.login_subtitle else R.string.register_subtitle,
                ),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth(.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AuthFormCard(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    isLogin: Boolean,
    focusManager: androidx.compose.ui.focus.FocusManager,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(.8f)
            .clip(RoundedCornerShape(Dimens.InputRadius))
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        GoogleButton()

        AuthDivider(isLogin = isLogin)

        AuthTextField(
            value = state.email,
            onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
            label = stringResource(R.string.email),
            error = state.errors["email"],
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        AuthTextField(
            value = state.password,
            onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
            label = stringResource(R.string.password),
            error = state.errors["password"],
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = if (isLogin) ImeAction.Done else ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onDone = if (isLogin) {
                    { focusManager.clearFocus(); onEvent(AuthEvent.Login) }
                } else null,
                onNext = if (!isLogin) {
                    { focusManager.moveFocus(FocusDirection.Down) }
                } else null,
            ),
        )

        AnimatedVisibility(
            visible = !isLogin,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
        ) {
            Column {
                AuthTextField(
                    value = state.confirmPassword,
                    onValueChange = { onEvent(AuthEvent.ConfirmPasswordChanged(it)) },
                    label = stringResource(R.string.confirm_password),
                    error = state.errors["confirmPassword"],
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onEvent(AuthEvent.Register)
                        },
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        AnimatedVisibility(
            visible = isLogin,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
        ) {
            TextButton(
                onClick = { /* Forgot password */ },
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary,
                )
            }
        }
    }
}

@Composable
private fun GoogleButton() {
    OutlinedButton(
        onClick = { /* Google auth placeholder */ },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.InputRadius),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Google",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun AuthDivider(isLogin: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
        )
        AnimatedContent(
            targetState = isLogin,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "divider_text",
        ) { login ->
            Text(
                text = stringResource(
                    if (login) R.string.or_login_with else R.string.or_signup_with,
                ),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        HorizontalDivider(
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun AuthActionButton(
    state: AuthUiState,
    isLogin: Boolean,
    onEvent: (AuthEvent) -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager,
) {
    Button(
        onClick = {
            focusManager.clearFocus()
            if (isLogin) onEvent(AuthEvent.Login) else onEvent(AuthEvent.Register)
        },
        enabled = if (isLogin) state.canLogin else state.canRegister,
        modifier = Modifier
            .fillMaxWidth(.8f)
            .height(60.dp),
        shape = RoundedCornerShape(Dimens.ButtonRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            AnimatedContent(
                targetState = isLogin,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "button_text",
            ) { login ->
                Text(
                    text = stringResource(
                        if (login) R.string.sign_in else R.string.create_account,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun AuthToggleText(
    isLogin: Boolean,
    onToggleMode: () -> Unit,
) {
    TextButton(onClick = onToggleMode) {
        AnimatedContent(
            targetState = isLogin,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "toggle_text",
        ) { login ->
            Text(
                text = stringResource(
                    if (login) R.string.dont_have_account else R.string.already_have_account,
                ) + " " + stringResource(
                    if (login) R.string.sign_up else R.string.sign_in,
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
