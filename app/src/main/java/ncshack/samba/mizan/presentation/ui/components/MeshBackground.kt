package ncshack.samba.mizan.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import io.github.om252345.composemeshgradient.MeshGradient
import io.github.om252345.composemeshgradient.rememberMeshGradientState
import io.github.om252345.composemeshgradient.utils.SimplexNoise
import kotlinx.coroutines.launch
import ncshack.samba.mizan.ui.theme.LightColorScheme
import kotlin.collections.toTypedArray

@Composable
fun MeshBackground(
    animating: Boolean,
    modifier: Modifier = Modifier
) {
    val width = 4
    val height = 4

    val colors = remember(Unit) {
        listOf(
            LightColorScheme.primaryContainer.copy(alpha = 0.25f).compositeOver(LightColorScheme.background),
            LightColorScheme.secondaryContainer.copy(alpha = 0.2f).compositeOver(LightColorScheme.background),
            LightColorScheme.tertiaryContainer.copy(alpha = 0.15f).compositeOver(LightColorScheme.background),
            LightColorScheme.primaryContainer.copy(alpha = 0.2f).compositeOver(LightColorScheme.background),
            LightColorScheme.surfaceVariant.copy(alpha = 0.25f).compositeOver(LightColorScheme.background),
            LightColorScheme.secondaryContainer.copy(alpha = 0.15f).compositeOver(LightColorScheme.background),
            LightColorScheme.tertiaryContainer.copy(alpha = 0.2f).compositeOver(LightColorScheme.background),
            LightColorScheme.primary.copy(alpha = 0.04f).compositeOver(LightColorScheme.background),
            LightColorScheme.secondary.copy(alpha = 0.04f).compositeOver(LightColorScheme.background),
            LightColorScheme.primaryContainer.copy(alpha = 0.15f).compositeOver(LightColorScheme.background),
            LightColorScheme.surfaceVariant.copy(alpha = 0.2f).compositeOver(LightColorScheme.background),
            LightColorScheme.tertiaryContainer.copy(alpha = 0.1f).compositeOver(LightColorScheme.background),
            LightColorScheme.secondaryContainer.copy(alpha = 0.25f).compositeOver(LightColorScheme.background),
            LightColorScheme.primaryContainer.copy(alpha = 0.1f).compositeOver(LightColorScheme.background),
            LightColorScheme.tertiary.copy(alpha = 0.04f).compositeOver(LightColorScheme.background),
            LightColorScheme.surfaceVariant.copy(alpha = 0.15f).compositeOver(LightColorScheme.background),
        ).toTypedArray()
    }

    val initialPoints = remember {
        Array(width * height) { i ->
            val col = i % width
            val row = i / width
            Offset(x = col / (width - 1f), y = row / (height - 1f))
        }
    }

    val meshState = rememberMeshGradientState(points = initialPoints, colors = colors)

    LaunchedEffect(animating) {
        var time = 0f
        val basePoints = initialPoints.toList()
        val currentPoints = initialPoints.toMutableList()
        val targetPoints = initialPoints.toMutableList()
        var lastFrameTime = 0L

        while (animating) {
            withFrameNanos { frameTime ->
                if (lastFrameTime == 0L) lastFrameTime = frameTime
                val deltaTime = (frameTime - lastFrameTime) / 1_000_000_000.0f
                lastFrameTime = frameTime
                time += deltaTime * 0.3f // Animation speed

                for (i in targetPoints.indices) {
                    val col = i % width
                    val row = i / width
                    val isBorder = row == 0 || row == height - 1 || col == 0 || col == width - 1

                    if (!isBorder) {
                        val bp = basePoints[i]
                        val noiseX = SimplexNoise.noise(bp.x * 1.5f, time + i) * 0.2f
                        val noiseY = SimplexNoise.noise(bp.y * 1.5f, time + i + 100f) * 0.2f
                        targetPoints[i] = Offset(bp.x + noiseX, bp.y + noiseY)
                    }
                }

                for (i in currentPoints.indices) {
                    val lerped = lerp(currentPoints[i], targetPoints[i], (8f * deltaTime))
                    currentPoints[i] = lerped
                }

                launch {
                    meshState.snapAllPoints(currentPoints.toList())
                }
            }
        }
    }

    MeshGradient(
        modifier = modifier,
        width = width,
        height = height,
        points = meshState.points.toTypedArray(),
        colors = colors
    )
}