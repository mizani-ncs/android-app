package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FiberManualRecord
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.ui.graphics.Color

enum class CardColor(val seed: Color, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    GREEN(
        seed = Color(0xFF2E7D32),
        icon = androidx.compose.material.icons.Icons.Rounded.CheckCircle,
    ),
    RED(
        seed = Color(0xFFC62828),
        icon = androidx.compose.material.icons.Icons.Rounded.Warning,
    ),
    BLUE(
        seed = Color(0xFF1565C0),
        icon = androidx.compose.material.icons.Icons.Rounded.Info,
    ),
    PURPLE(
        seed = Color(0xFF6A1B9A),
        icon = androidx.compose.material.icons.Icons.Rounded.DateRange,
    ),
    AMBER(
        seed = Color(0xFFF57F17),
        icon = androidx.compose.material.icons.Icons.Rounded.QuestionMark,
    ),
    TEAL(
        seed = Color(0xFF00695C),
        icon = androidx.compose.material.icons.Icons.Rounded.Person,
    ),
    INDIGO(
        seed = Color(0xFF283593),
        icon = androidx.compose.material.icons.Icons.Rounded.FavoriteBorder,
    ),
    ORANGE(
        seed = Color(0xFFE06D2E),
        icon = androidx.compose.material.icons.Icons.Rounded.Email,
    ),
    CYAN(
        seed = Color(0xFF00838F),
        icon = androidx.compose.material.icons.Icons.Rounded.Search,
    ),
    GRAY(
        seed = Color(0xFF546E7A),
        icon = androidx.compose.material.icons.Icons.Rounded.FiberManualRecord,
    ),
    DEEP_PURPLE(
        seed = Color(0xFF4527A0),
        icon = androidx.compose.material.icons.Icons.Rounded.AccountBalance,
    );

    companion object {
        fun fromCardType(cardType: String): CardColor = when (cardType) {
            "text" -> GRAY
            "info" -> BLUE
            "lawyer_carousel" -> TEAL
            "deadline" -> RED
            "context" -> PURPLE
            "step" -> AMBER
            "legal_analysis" -> DEEP_PURPLE
            "knowledge_result" -> GREEN
            "action_required" -> ORANGE
            "booking" -> CYAN
            "submit_document" -> INDIGO
            "download_file" -> INDIGO
            else -> GRAY
        }
    }
}
