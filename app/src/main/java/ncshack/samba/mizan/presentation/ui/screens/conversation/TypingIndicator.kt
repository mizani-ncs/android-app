package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ncshack.samba.mizan.ui.theme.Primary

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val dots = listOf(
        remember { Animatable(0.3f) },
        remember { Animatable(0.3f) },
        remember { Animatable(0.3f) },
    )

    LaunchedEffect(Unit) {
        dots.forEachIndexed { index, dot ->
            launch {
                delay(index * 150L)
                dot.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = 600
                            0.3f at 0
                            1f at 300
                            0.3f at 600
                        },
                    ),
                )
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .shadow(20.dp, CircleShape, ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.05f))
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(2.dp, 24.dp, 24.dp, 24.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 2.dp,
            modifier = Modifier.width(76.dp),
        ) {
            Row(
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                dots.forEach { dot ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .alpha(dot.value)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface),
                    )
                }
            }
        }
    }
}
