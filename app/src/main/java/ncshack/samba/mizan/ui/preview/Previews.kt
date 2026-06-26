package ncshack.samba.mizan.ui.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ncshack.samba.mizan.domain.model.CardDescriptor
import ncshack.samba.mizan.presentation.ui.screens.auth.AuthScreen
import ncshack.samba.mizan.presentation.ui.screens.conversation.ActionRequiredCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.BookingCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.ClarifyingChipsCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.ContextCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.ConversationScreen
import ncshack.samba.mizan.presentation.ui.screens.conversation.DeadlineCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.DownloadFileCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.InfoCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.KnowledgeResultCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.LawyerCarouselCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.LegalAnalysisCard
import ncshack.samba.mizan.presentation.ui.screens.conversation.MessageBubble
import ncshack.samba.mizan.presentation.ui.screens.conversation.MessageInput
import ncshack.samba.mizan.presentation.ui.screens.conversation.ShimmerCardSkeleton
import ncshack.samba.mizan.presentation.ui.screens.conversation.SubmitDocumentCard
import ncshack.samba.mizan.presentation.viewmodel.AuthUiState
import ncshack.samba.mizan.presentation.viewmodel.ConversationItem
import ncshack.samba.mizan.presentation.viewmodel.ConversationUiState
import ncshack.samba.mizan.ui.theme.MizanTheme

// ──────────────────────────────────────────────
// Auth
// ──────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Login")
@Composable
private fun AuthLoginPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(email = "user@example.com", password = "password123"),
            onEvent = {},
            isLogin = true,
            onToggleMode = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Register")
@Composable
private fun AuthRegisterPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(email = "newuser@example.com", password = "securePass!", confirmPassword = "securePass!"),
            onEvent = {},
            isLogin = false,
            onToggleMode = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Loading")
@Composable
private fun AuthLoadingPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(email = "user@example.com", password = "password123", isLoading = true),
            onEvent = {},
            isLogin = true,
            onToggleMode = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Auth — Errors")
@Composable
private fun AuthErrorsPreview() {
    MizanTheme {
        AuthScreen(
            state = AuthUiState(
                email = "bad-email",
                password = "123",
                errors = mapOf("email" to "Invalid email", "password" to "Password must be at least 8 characters"),
            ),
            onEvent = {},
            isLogin = true,
            onToggleMode = {},
        )
    }
}

// ──────────────────────────────────────────────
// Conversation — Empty / Typing
// ──────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Conversation — Empty")
@Composable
private fun ConversationEmptyPreview() {
    MizanTheme {
        ConversationScreen(
            state = ConversationUiState(),
            onIntent = {},
            onNavigateToLawyerProfile = {},
            onNavigateToBooking = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Conversation — Typing")
@Composable
private fun ConversationTypingPreview() {
    MizanTheme {
        ConversationScreen(
            state = ConversationUiState(
                items = listOf(ConversationItem.UserMessage(id = "1", text = "What are my rights?")),
                isAwaitingResponse = true,
            ),
            onIntent = {},
            onNavigateToLawyerProfile = {},
            onNavigateToBooking = {},
        )
    }
}

// ──────────────────────────────────────────────
// Conversation — Full Chat with All Card Types
// ──────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "Conversation — Full Chat")
@Composable
private fun ConversationFullChatPreview() {
    MizanTheme {
        ConversationScreen(
            state = ConversationUiState(
                items = listOf(
                    ConversationItem.UserMessage(id = "u1", text = "Mon employeur m'a licencié sans préavis. Quels sont mes droits?"),

                    ConversationItem.UserMessage(
                        id = "ai_text_1",
                        text = "Je comprends votre situation. Votre employeur vous a licencié sans préavis. Selon la loi 90-11, cela peut constituer un licenciement abusif. Laissez-moi chercher les options qui s'offrent à vous.",
                        isUser = false,
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_context",
                        cards = listOf(
                            CardDescriptor(
                                id = "ctx_1",
                                cardType = "context",
                                payload = """{"text":"Selon l'article 73 de la loi 90-11 du 21 avril 1990, tout licenciement sans préavis est considéré comme abusif. Le salarié a droit à une indemnité compensatrice.","disclaimer":"Ceci n'est pas un avis juridique. Consultez un avocat pour des conseils personnalisés.","language":"fr"}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_step",
                        cards = listOf(
                            CardDescriptor(
                                id = "step_1",
                                cardType = "step",
                                payload = """{"prompt":"Avez-vous un contrat de travail écrit ?","options":["Oui, j'ai un contrat","Non, rien par écrit","Je ne sais pas"]}""",
                            ),
                        ),
                    ),

                    ConversationItem.UserMessage(id = "u2", text = "Oui, j'ai un contrat de travail"),

                    ConversationItem.UserMessage(
                        id = "ai_text_2",
                        text = "Parfait. Avec un contrat écrit, vous avez des droits supplémentaires. Laissez-moi analyser votre situation en détail.",
                        isUser = false,
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_analysis",
                        cards = listOf(
                            CardDescriptor(
                                id = "analysis_1",
                                cardType = "legal_analysis",
                                payload = """{"title":"Licenciement abusif — analyse","sections":[{"heading":"Cadre légal","body":"La loi 90-11 du 21 avril 1990 régit les relations de travail en Algérie. L'article 73 stipule que le licenciement doit être justifié et assorti d'un préavis."},{"heading":"Vos droits","body":"En l'absence de préavis, vous avez droit à une indemnité compensatrice égale au salaire du period de préavis, plus une indemnité pour licenciement abusif."}],"citations":[{"lawId":"LOI-90-11","article":"73","text":"Tout licenciement sans préavis donne lieu à une indemnité compensatrice."},{"lawId":"LOI-90-11","article":"74","text":"Le juge peut ordonner la réintégration du salarié licencié abusivement."}],"disclaimer":"Ceci n'est pas un avis juridique.","language":"fr"}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_knowledge",
                        cards = listOf(
                            CardDescriptor(
                                id = "know_1",
                                cardType = "knowledge_result",
                                payload = """{"title":"Résultats — droit du travail","answer":"La loi 90-11 encadre les relations de travail. Le licenciement sans motif réel et sérieux est abusif. L'indemnité minimale est calculée sur la base du salaire et de l'ancienneté.","sources":[{"title":"Journal officiel — Loi 90-11","url":"https://www.joradp.dz/","snippet":"Art. 73 — Les relations de travail sont régies par le présent code."},{"title":"Guide pratique — Inspection du travail","snippet":"L'inspection du travail peut vous aider gratuitement."}],"confidence":0.92,"language":"fr"}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_info",
                        cards = listOf(
                            CardDescriptor(
                                id = "info_1",
                                cardType = "info",
                                payload = """{"title":"Le saviez-vous ?","source":"https://www.emploi.dz/","text":"L'inspection du travail peut vous aider à déposer une plainte gratuitement. Vous pouvez contacter l'inspection du travail de votre wilaya."}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_lawyers",
                        cards = listOf(
                            CardDescriptor(
                                id = "lawyer_1",
                                cardType = "lawyer_carousel",
                                payload = """{"lawyers":[{"id":"l1","name":"Me. Ahmed Benali","specialty":"Droit du travail","rating":4.8,"image":null},{"id":"l2","name":"Me. Fatima Zohra Khelifi","specialty":"Droit du travail","rating":4.5,"image":null},{"id":"l3","name":"Me. Youcef Amrani","specialty":"Droit civil","rating":4.9,"image":null}]}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_deadline",
                        cards = listOf(
                            CardDescriptor(
                                id = "deadline_1",
                                cardType = "deadline",
                                payload = """{"title":"Date limite de recours","date":"2026-07-15T00:00:00.000Z"}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_action",
                        cards = listOf(
                            CardDescriptor(
                                id = "action_1",
                                cardType = "action_required",
                                payload = """{"title":"Prochaine étape","description":"Vous pouvez déposer une plainte auprès de l'inspection du travail. Voulez-vous que je prépare le document ?","actions":[{"label":"Préparer le document","cardType":"submit_document","payload":{}},{"label":"Contacter un avocat","cardType":"lawyer_carousel","payload":{}}]}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_submit_doc",
                        cards = listOf(
                            CardDescriptor(
                                id = "doc_1",
                                cardType = "submit_document",
                                payload = """{"title":"Contrat de travail","description":"Téléversez une photo ou un scan de votre contrat de travail pour analyse.","acceptedTypes":["image/jpeg","image/png","application/pdf"]}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_booking",
                        cards = listOf(
                            CardDescriptor(
                                id = "book_1",
                                cardType = "booking",
                                payload = """{"lawyerId":"l1","lawyerName":"Me. Ahmed Benali","slots":["2026-07-20T10:00:00.000Z","2026-07-20T14:00:00.000Z","2026-07-21T09:00:00.000Z"]}""",
                            ),
                        ),
                    ),

                    ConversationItem.CardGroup(
                        id = "cards_download",
                        cards = listOf(
                            CardDescriptor(
                                id = "dl_1",
                                cardType = "download_file",
                                payload = """{"title":"Demande de mise en demeure","description":"Document généré à partir des informations que vous avez fournies.","documentType":"demand_letter","downloadUrl":"https://res.cloudinary.com/demo/demande.pdf"}""",
                            ),
                        ),
                    ),
                ),
                inputText = "Que dois-je faire maintenant ?",
            ),
            onIntent = {},
            onNavigateToLawyerProfile = {},
            onNavigateToBooking = {},
        )
    }
}

// ──────────────────────────────────────────────
// MessageBubble
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "MessageBubble — User")
@Composable
private fun MessageBubbleUserPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            MessageBubble(text = "Quels sont mes droits en tant que locataire en Algérie ?", isUser = true)
        }
    }
}

@Preview(showBackground = true, name = "MessageBubble — Assistant")
@Composable
private fun MessageBubbleAssistantPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            MessageBubble(
                text = "En vertu du droit algérien, les locataires bénéficient de plusieurs protections, notamment des délais raisonnables de préavis et une protection contre l'expulsion arbitraire.",
                isUser = false,
            )
        }
    }
}

// ──────────────────────────────────────────────
// MessageInput
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "MessageInput — Empty")
@Composable
private fun MessageInputEmptyPreview() {
    MizanTheme { Surface { MessageInput(value = "", onValueChange = {}, onSubmit = {}, enabled = true) } }
}

@Preview(showBackground = true, name = "MessageInput — With Text")
@Composable
private fun MessageInputTextPreview() {
    MizanTheme { Surface { MessageInput(value = "Puis-je résilier mon bail ?", onValueChange = {}, onSubmit = {}, enabled = true) } }
}

@Preview(showBackground = true, name = "MessageInput — Disabled")
@Composable
private fun MessageInputDisabledPreview() {
    MizanTheme { Surface { MessageInput(value = "En attente...", onValueChange = {}, onSubmit = {}, enabled = false) } }
}

// ──────────────────────────────────────────────
// ShimmerCardSkeleton
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "ShimmerCardSkeleton")
@Composable
private fun ShimmerCardSkeletonPreview() {
    MizanTheme { Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) { ShimmerCardSkeleton() } }
}

// ──────────────────────────────────────────────
// Cards — Individual
// ──────────────────────────────────────────────

@Preview(showBackground = true, name = "Card — ClarifyingChips (step)")
@Composable
private fun StepCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ClarifyingChipsCard(
                card = CardDescriptor(
                    id = "step_1",
                    cardType = "step",
                    payload = """{"prompt":"Avez-vous un contrat de travail écrit ?","options":["Oui, j'ai un contrat","Non, rien par écrit","Je ne sais pas"]}""",
                ),
                onChipSelected = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Context")
@Composable
private fun ContextCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ContextCard(
                card = CardDescriptor(
                    id = "ctx_1",
                    cardType = "context",
                    payload = """{"text":"Selon l'article 73 de la loi 90-11 du 21 avril 1990, tout licenciement sans préavis est considéré comme abusif.","disclaimer":"Ceci n'est pas un avis juridique. Consultez un avocat.","language":"fr"}""",
                ),
                onDismiss = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Info")
@Composable
private fun InfoCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            InfoCard(
                card = CardDescriptor(
                    id = "info_1",
                    cardType = "info",
                    payload = """{"title":"Le saviez-vous ?","source":"https://www.emploi.dz/","text":"L'inspection du travail peut vous aider à déposer une plainte gratuitement."}""",
                ),
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — LawyerCarousel")
@Composable
private fun LawyerCarouselCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LawyerCarouselCard(
                card = CardDescriptor(
                    id = "lawyer_1",
                    cardType = "lawyer_carousel",
                    payload = """{"lawyers":[{"id":"l1","name":"Me. Ahmed Benali","specialty":"Droit du travail","rating":4.8,"image":null},{"id":"l2","name":"Me. Fatima Zohra Khelifi","specialty":"Droit pénal","rating":4.5,"image":null},{"id":"l3","name":"Me. Youcef Amrani","specialty":"Droit des affaires","rating":4.9,"image":null}]}""",
                ),
                onNavigateToLawyerProfile = {},
                onNavigateToBooking = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Booking")
@Composable
private fun BookingCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            BookingCard(
                card = CardDescriptor(
                    id = "book_1",
                    cardType = "booking",
                    payload = """{"lawyerId":"l1","lawyerName":"Me. Ahmed Benali","slots":["2026-07-20T10:00:00.000Z","2026-07-20T14:00:00.000Z","2026-07-21T09:00:00.000Z"]}""",
                ),
                onBookingConfirmed = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — Deadline")
@Composable
private fun DeadlineCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            DeadlineCard(
                card = CardDescriptor(
                    id = "deadline_1",
                    cardType = "deadline",
                    payload = """{"title":"Date limite de recours","date":"2026-07-15T00:00:00.000Z"}""",
                ),
                onAddedToCalendar = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — SubmitDocument")
@Composable
private fun SubmitDocumentCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            SubmitDocumentCard(
                card = CardDescriptor(
                    id = "doc_1",
                    cardType = "submit_document",
                    payload = """{"title":"Contrat de travail","description":"Téléversez une photo ou un scan de votre contrat de travail.","acceptedTypes":["image/jpeg","image/png","application/pdf"]}""",
                ),
                onUploadTap = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — DownloadFile")
@Composable
private fun DownloadFileCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            DownloadFileCard(
                card = CardDescriptor(
                    id = "dl_1",
                    cardType = "download_file",
                    payload = """{"title":"Demande de mise en demeure","description":"Document généré à partir des informations fournies.","documentType":"demand_letter","downloadUrl":"https://res.cloudinary.com/demo/demande.pdf"}""",
                ),
                onDownload = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — LegalAnalysis")
@Composable
private fun LegalAnalysisCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LegalAnalysisCard(
                card = CardDescriptor(
                    id = "analysis_1",
                    cardType = "legal_analysis",
                    payload = """{"title":"Licenciement abusif — analyse","sections":[{"heading":"Cadre légal","body":"La loi 90-11 du 21 avril 1990 régit les relations de travail en Algérie."},{"heading":"Vos droits","body":"Vous avez droit à une indemnité compensatrice et potentiellement à la réintégration."}],"citations":[{"lawId":"LOI-90-11","article":"73","text":"Tout licenciement sans préavis donne lieu à une indemnité."}],"disclaimer":"Ceci n'est pas un avis juridique.","language":"fr"}""",
                ),
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — KnowledgeResult")
@Composable
private fun KnowledgeResultCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            KnowledgeResultCard(
                card = CardDescriptor(
                    id = "know_1",
                    cardType = "knowledge_result",
                    payload = """{"title":"Résultats — droit du travail","answer":"La loi 90-11 encadre les relations de travail. Le licenciement sans motif réel et sérieux est abusif.","sources":[{"title":"Journal officiel — Loi 90-11","url":"https://www.joradp.dz/","snippet":"Art. 73 — Les relations de travail sont régies par le présent code."}],"confidence":0.92,"language":"fr"}""",
                ),
            )
        }
    }
}

@Preview(showBackground = true, name = "Card — ActionRequired")
@Composable
private fun ActionRequiredCardPreview() {
    MizanTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            ActionRequiredCard(
                card = CardDescriptor(
                    id = "action_1",
                    cardType = "action_required",
                    payload = """{"title":"Prochaine étape","description":"Vous pouvez déposer une plainte auprès de l'inspection du travail.","actions":[{"label":"Préparer le document","cardType":"submit_document","payload":{}},{"label":"Contacter un avocat","cardType":"lawyer_carousel","payload":{}}]}""",
                ),
                onActionTapped = {},
            )
        }
    }
}
