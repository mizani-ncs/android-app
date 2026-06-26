package ncshack.samba.mizan.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(Dimens.ButtonRadius),      // 5dp from Figma
    medium = RoundedCornerShape(Dimens.CardRadius),       // 12dp M3 medium shape token
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)