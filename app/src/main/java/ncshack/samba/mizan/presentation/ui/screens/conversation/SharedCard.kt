package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.materialkolor.DynamicMaterialTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SharedCard(
    title: String,
    cardColor: CardColor,
    modifier: Modifier = Modifier,
    badge: String? = null,
    icon: ImageVector = cardColor.icon,
    content: @Composable ColumnScope.() -> Unit,
) {
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible.value = true }

    DynamicMaterialTheme(
        seedColor = cardColor.seed,
        isDark = false,
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = visible.value,
                    enter = fadeIn(animationSpec = tween(delayMillis = 0)) +
                            slideInVertically(animationSpec = tween(delayMillis = 0)) { it / 4 },
                ) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = MaterialShapes.Cookie12Sided.toShape(),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = visible.value,
                    enter = fadeIn(animationSpec = tween(delayMillis = 100)) +
                            slideInVertically(animationSpec = tween(delayMillis = 100)) { it / 4 },
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth(.8f).align(Alignment.CenterHorizontally),
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth(),
                    visible = visible.value,
                    enter = fadeIn(animationSpec = tween(delayMillis = 200)) +
                            slideInVertically(animationSpec = tween(delayMillis = 200)) { it / 4 },
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        content()
                    }
                }
            }
        }
    }
}
