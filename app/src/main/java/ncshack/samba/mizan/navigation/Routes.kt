package ncshack.samba.mizan.navigation

import kotlinx.serialization.Serializable

@Serializable data object Auth
@Serializable data object MainGraph
@Serializable data object Conversation
@Serializable data object Documents
@Serializable data class LawyerProfile(val lawyerId: String)
@Serializable data class Booking(val lawyerId: String)
