package ncshack.samba.mizan.presentation.ui.screens.conversation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.ExperimentalHazeApi
import dev.chrisbanes.haze.HazeInputScale
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ncshack.samba.mizan.R
import ncshack.samba.mizan.domain.model.Session
import ncshack.samba.mizan.presentation.ui.components.MeshBackground
import ncshack.samba.mizan.presentation.ui.components.expressiveListItemShape
import ncshack.samba.mizan.presentation.viewmodel.ConversationIntent
import ncshack.samba.mizan.presentation.viewmodel.ConversationItem
import ncshack.samba.mizan.presentation.viewmodel.ConversationUiState
import ncshack.samba.mizan.ui.theme.Primary
import androidx.compose.ui.focus.FocusRequester

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeApi::class)
@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onIntent: (ConversationIntent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNavigateToLawyerProfile: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit,
    onNavigateToAuth: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val inputFocusRequester = remember { FocusRequester() }

    LaunchedEffect(state.items.size) {
        if (state.items.isNotEmpty()) {
            listState.animateScrollToItem(state.items.size - 1)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SessionDrawerContent(
                sessions = state.sessions,
                currentSessionId = state.currentSessionId,
                onSessionSelected = { sessionId ->
                    onIntent(ConversationIntent.SelectSession(sessionId))
                    scope.launch { drawerState.close() }
                },
                onNewSession = {
                    onIntent(ConversationIntent.NewSession)
                    scope.launch { drawerState.close() }
                },
                onLogout = {
                    onIntent(ConversationIntent.Logout)
                }
            )
        },
    ) {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
            MeshBackground(
                //state.isAwaitingResponse
                true,
                Modifier.fillMaxSize()
            )
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.systemBars),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(R.drawable.logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    colorFilter = ColorFilter.tint(Primary),
                                )
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.History, null)
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = null,
                                )
                            }
                            Box(
                                Modifier
                                    .padding(end = 8.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .align(Alignment.Center),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        expandedHeight = 80.dp,
                        modifier = Modifier.hazeEffect(state = rememberHazeState()) {
                            inputScale = HazeInputScale.Auto
                            drawContentBehind = true
                            canDrawArea = { true }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = Primary,
                            navigationIconContentColor = Primary
                        ),
                    )
                },
                containerColor = Color.Transparent,
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                ) {
                    AnimatedContent(
                        state.items.isEmpty() && !state.isAwaitingResponse,
                        Modifier.fillMaxSize()
                    ) {
                        if (it) {
                            WelcomeContent(onIntent = onIntent, inputFocusRequester = inputFocusRequester)
                        } else {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = WindowInsets.navigationBars.add(
                                    WindowInsets(
                                        bottom = 140.dp,
                                        left = 16.dp,
                                        right = 16.dp
                                    )
                                ).asPaddingValues(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(
                                    items = state.items,
                                    key = { it.id },
                                ) { item ->
                                    Box(modifier = Modifier.animateItem()) {
                                        when (item) {
                                            is ConversationItem.UserMessage -> MessageBubble(
                                                text = item.text,
                                                isUser = item.isUser,
                                            )
                                            is ConversationItem.CardGroup -> CardGroup(
                                                cards = item.cards,
                                                onCardActionTapped = { text ->
                                                    onIntent(ConversationIntent.CardActionTapped(text))
                                                },
                                                onNavigateToLawyerProfile = onNavigateToLawyerProfile,
                                                onNavigateToBooking = onNavigateToBooking,
                                            )
                                        }
                                    }
                                }

                                if (state.isAwaitingResponse) {
                                    item(key = "typing_indicator") {
                                        ShimmerCardSkeleton(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (state.isConnectionError) {
                        ConnectionErrorInput(
                            onRetry = { onIntent(ConversationIntent.RetryConnection) },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding(),
                        )
                    } else if (state.isLoadingSessions) {
                        ConnectionLoadingInput(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding(),
                        )
                    } else {
                        MessageInput(
                            value = state.inputText,
                            onValueChange = { onIntent(ConversationIntent.TextChanged(it)) },
                            onSubmit = { onIntent(ConversationIntent.Submit) },
                            enabled = true,
                            focusRequester = inputFocusRequester,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding(),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun WelcomeContent(
    onIntent: (ConversationIntent) -> Unit,
    inputFocusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val suggestions = remember { suggestionPool }
    val pageSize = 3
    var pageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3_000)
            pageIndex = (pageIndex + 1) % suggestions.size
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = 160.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier.size(200.dp), contentAlignment = Alignment.Center) {
            LoadingIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.2f),
                color = MaterialTheme.colorScheme.primaryContainer,
                polygons = listOf(
                    MaterialShapes.Sunny,
                    MaterialShapes.VerySunny,
                    MaterialShapes.Cookie12Sided,
                    MaterialShapes.Cookie4Sided,
                ),
            )
            Icon(
                Icons.Default.Person,
                null,
                modifier = Modifier.fillMaxSize(.3f),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Bonjour, utilisateur!",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Voici quelques suggestions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(28.dp))
        val currentIndices = remember(pageIndex) {
            (0 until pageSize).map { i -> (pageIndex + i) % suggestions.size }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        ) {
            items(
                items = currentIndices,
                key = { it },
            ) { idx ->
                val suggestion = suggestions[idx]
                    Surface(
                        onClick = {
                            onIntent(ConversationIntent.TextChanged(suggestion.detailedPrompt))
                            inputFocusRequester.requestFocus()
                        },
                    shape = RoundedCornerShape(100),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    tonalElevation = 2.dp,
                    modifier = Modifier.animateItem().widthIn(max = 240.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        Icon(
                            imageVector = suggestion.icon,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = suggestion.text,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

private data class Suggestion(
    val icon: ImageVector,
    val text: String,
    val detailedPrompt: String,
)

private val suggestionPool = listOf(
    Suggestion(
        Icons.Default.Gavel, "Procédures de divorce",
        "Je souhaite connaître les procédures de divorce en Algérie. Je suis marié depuis [durée] ans, nous avons [nombre] enfants, et je réside à [wilaya]. Mon/Ma conjoint(e) est d'accord pour un divorce par consentement mutuel ? [oui/non]. Je voudrais savoir comment se déroule la procédure, les documents nécessaires, et la durée estimée.",
    ),
    Suggestion(
        Icons.Default.AccountBalance, "Droit d'héritage",
        "J'ai besoin d'informations sur le droit d'héritage en Algérie. Le défunt est mon [relation: père/mère/époux/épouse/autre], décédé(e) le [date]. Il/Elle possédait des biens à [wilaya] incluant [type de biens: maison/terrain/compte bancaire]. Les héritiers sont : [liste des héritiers]. Je souhaite connaître ma part d'héritage selon le droit algérien et les démarches à suivre.",
    ),
    Suggestion(
        Icons.Default.Work, "Conflit de travail",
        "Je fais face à un conflit de travail avec mon employeur. Je travaille dans [secteur d'activité] depuis [durée]. Le problème est : [licenciement abusif / non-paiement de salaire / harcèlement / autre]. Mon contrat de travail est [CDI/CDD/sans contrat]. Je souhaite connaître mes droits et les recours possibles en droit du travail algérien.",
    ),
    Suggestion(
        Icons.Default.House, "Litige foncier",
        "J'ai un litige foncier concernant un terrain situé à [wilaya/commune]. Le terrain fait [superficie] m² et est [bâti/non bâti]. Le conflit m'oppose à [voisin/famille/administration/municipalité]. Je possède [titre de propriété/acte notarié/aucun document]. Les faits remontent à [date/période]. Je voudrais savoir comment régler ce litige et quelles sont les procédures judiciaires.",
    ),
    Suggestion(
        Icons.Default.Person, "Garde d'enfants",
        "Je suis concerné(e) par une procédure de garde d'enfants. J'ai [nombre] enfant(s) âgé(s) de [âges]. Je suis en instance de divorce/séparé(e) depuis [durée]. Je réside à [wilaya] et l'autre parent réside à [wilaya]. Je souhaite obtenir la garde principale de mon/mes enfant(s). Quels sont mes droits et les démarches à suivre selon le droit de la famille algérien ?",
    ),
    Suggestion(
        Icons.Default.Description, "Contrat de location",
        "Je souhaite faire réviser/comprendre un contrat de location. Le bien est situé à [wilaya/commune], le loyer est de [montant] DZD par mois, et la durée du bail est de [durée]. Je suis le [propriétaire/locataire]. Le contrat a été signé le [date]. J'ai des questions concernant [clause spécifique / augmentation de loyer / résiliation / dépôt de garantie].",
    ),
    Suggestion(
        Icons.Default.Business, "Création d'entreprise",
        "Je souhaite créer une entreprise en Algérie. Mon projet est dans le secteur [secteur d'activité] et je suis situé(e) à [wilaya]. La forme juridique envisagée est [EURL/SPA/SARL/entreprise individuelle]. Mon capital de départ est d'environ [montant] DZD. Je voudrais connaître les démarches administratives, les documents requis, les délais d'immatriculation et les obligations fiscales.",
    ),
    Suggestion(
        Icons.Default.ShoppingCart, "Droits du consommateur",
        "J'ai un problème en tant que consommateur. J'ai acheté un [produit/service] le [date] au prix de [montant] DZD chez [nom du vendeur/magasin] à [wilaya]. Le problème est : [défaut/non-conformité/retard de livraison/refus de remboursement]. J'ai [oui/non] le ticket de caisse. Quels sont mes droits en tant que consommateur en Algérie et comment puis-je porter plainte ?",
    ),
    Suggestion(
        Icons.Default.DirectionsCar, "Accident de la route",
        "J'ai été impliqué(e) dans un accident de la route le [date] à [lieu]. Je suis [conducteur/passager/piéton]. L'autre conducteur est [identifié/non identifié]. Il y a [blessés/dégâts matériels]. Un constat a été [signé/non signé]. Mon assurance est [nom de l'assurance]. Je souhaite connaître la procédure à suivre, mes droits à indemnisation, et les délais légaux.",
    ),
    Suggestion(
        Icons.Default.Favorite, "Mariage et dot",
        "Je souhaite me renseigner sur les aspects légaux du mariage en Algérie. Je suis [homme/femme], âgé(e) de [âge], résidant à [wilaya]. Mon/ma futur(e) conjoint(e) est de nationalité [algérienne/étrangère]. Nous souhaitons connaître les documents requis, la procédure de mariage civil, les règles concernant la dot (mahr), et le régime matrimonial.",
    ),
    Suggestion(
        Icons.Default.Lock, "Droit pénal",
        "Je suis impliqué(e) dans une affaire pénale. Je suis [victime/prévenu/témoin] dans une affaire de [nature de l'affaire] qui s'est produite le [date] à [lieu]. Une plainte a été [déposée/non déposée] auprès du tribunal de [wilaya]. Je souhaite connaître mes droits, la procédure pénale en Algérie, et les peines encourues.",
    ),
    Suggestion(
        Icons.Default.MonetizationOn, "Dettes et recouvrement",
        "Je fais face à un problème de dettes. Je dois [montant] DZD à [créancier: banque/particulier/entreprise] pour [raison de la dette]. La dette date de [période]. Je reçois des [relances/mises en demeure]. Je suis [en mesure de payer/non en mesure de payer]. Je voudrais connaître les options de recouvrement, les délais de prescription et les solutions de règlement.",
    ),
    Suggestion(
        Icons.Default.Info, "Droits des victimes",
        "Je suis victime d'une infraction. Les faits se sont déroulés le [date] à [lieu]. Il s'agit de [nature de l'infraction: vol/agression/escroquerie/autre]. J'ai [déposé/non déposé] une plainte au commissariat de [wilaya]. Je souhaite connaître les droits des victimes en Algérie, les démarches pour obtenir réparation, et les aides disponibles.",
    ),
    Suggestion(
        Icons.Default.Flight, "Droit d'asile",
        "Je souhaite obtenir des informations sur le droit d'asile. Je suis de nationalité [nationalité] et je réside actuellement à [wilaya]. Ma demande d'asile est [déjà déposée/en cours d'instruction/non encore déposée]. Les motifs de ma demande sont : [persécutions politiques/religieuses/ethniques/conflit armé]. Je souhaite connaître la procédure, mes droits pendant l'instruction, et les recours en cas de refus.",
    ),
    Suggestion(
        Icons.Default.Star, "Pension alimentaire",
        "J'ai besoin d'informations sur la pension alimentaire en Algérie. Je suis [le parent créancier/le parent débiteur]. Nous avons [nombre] enfant(s). Le jugement de divorce a été rendu le [date] par le tribunal de [wilaya]. La pension alimentaire actuelle est de [montant] DZD. Je souhaite [réclamer une pension/augmenter la pension/contester le montant]. Quelles sont les démarches ?",
    ),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun SessionDrawerContent(
    sessions: List<Session>,
    currentSessionId: String?,
    onSessionSelected: (String) -> Unit,
    onNewSession: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .systemBarsPadding()
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TextButton(
            onNewSession, Modifier.fillMaxWidth(), colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Edit, null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("New Session")
            }
        }
        TextButton(
            onNewSession, Modifier.fillMaxWidth(), colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Search, null, Modifier.size(ButtonDefaults.IconSize))
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text("Search Discussions")
            }
        }
        if (sessions.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
            ) {
                Surface(
                    modifier = Modifier.size(160.dp),
                    shape = MaterialShapes.Cookie12Sided.toShape(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.surface,
                        )
                    }
                }
                Text(
                    text = "Aucune session précédente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(sessions, { _, s -> s.id }) { index, session ->
                    val isSelected = session.id == currentSessionId
                    val shape = expressiveListItemShape(index, sessions.size)
                    Row(
                        modifier = Modifier
                            .clip(shape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceContainer
                            )
                            .clickable(onClick = { onSessionSelected(session.id) })
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Text(
                            text = "Session at: ${session.createdAt}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "Utilisateur",
                Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton({}, colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)) {
                Icon(Icons.Outlined.Settings, null)
            }
            IconButton(
                onClick = onLogout,
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
            }
        }
    }
}
