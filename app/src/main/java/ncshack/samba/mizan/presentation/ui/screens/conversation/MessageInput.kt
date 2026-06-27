package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.R
import ncshack.samba.mizan.ui.theme.Primary

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val hasText = value.isNotBlank()
    val shape = RoundedCornerShape(100)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(
                    color = if (enabled) MaterialTheme.colorScheme.surfaceContainer
                    else MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = shape,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(start = 6.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = { /* attach */ },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Attach",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.ask_lexify),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = enabled,
                        textStyle = LocalTextStyle.current.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            textAlign = TextAlign.Start,
                        ),
                        cursorBrush = SolidColor(Primary),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (hasText) {
                                    focusManager.clearFocus()
                                    onSubmit()
                                }
                            },
                        ),
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        focusManager.clearFocus()
                        onSubmit()
                    },
                    enabled = hasText && enabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        contentColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}
