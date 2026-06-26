# Mizan — Task Breakdown

## Figma Design Reference

**Source**: `https://www.figma.com/design/PuqY2IdnvIyfZCgqdHvssa/NCShack?node-id=57-529`
**Canvas**: `😁 Pages` (orange-themed frames only — skip black "Home Screen" frames)

### Extracted Color Palette → M3 Theme Seed

| Token | Figma Value | M3 Role |
|---|---|---|
| `#e8380d` | Primary buttons, headers, "Lexify" logo | `primary` |
| `#dd3003` | Accent elements (calendar) | `tertiary` |
| `#f6f8fa` | Auth screen background | `background` |
| `#f7f7f7` | Conversation screen background | `surface` |
| `#ffffff` | Card surfaces, input fields | `surfaceContainer` |
| `#fbf9f9` | TopAppBar, calendar areas | `surfaceVariant` |
| `#171717` | Primary text | `onSurface` |
| `#8c8c8c` | Secondary/placeholder text | `onSurfaceVariant` |
| `#eff0f6` | Input field stroke | `outline` |
| `#22333c` | Dark UI elements | `outlineVariant` |
| `#50c878` | Success state | M3 derived from seed |
| `#111827` | Home indicator / UI chrome | `onSurface` (dark) |

Use these as seed for `lightColorScheme()`. Dynamic color disabled per PRD.

### Screen Structure (Orange-Themed Frames)

| Stage | Frame Size | Purpose | Key Elements |
|---|---|---|---|
| Login | 375×812 | Auth: email + password | Orange head, white inputs, `#e8380d` button, R5 radius |
| Sign Up | 375×812 | Auth: create account | Same structure as Login |
| Stage 1 | 390×1211 | Conversation start | TopAppBar `#fbf9f9` + "Lexify" logo, white bg |
| Stage 2 | 390×1002 | User sends message | Message bubble, input bar |
| Stage 3 | 390×935 | Clarifying questions | Chip Row with FilterChip |
| Stage 4 | 390×1177 | User response + cards | Card group with multiple card types |
| Stage 5 | 390×972 | Legal grounding | Context card with citation + disclaimer |
| Stage 6 | 390×1853 | Document upload | File picker + photo capture + upload state |
| Stage 7 | 390×990 | Lawyer matching | Horizontal lawyer carousel (LazyRow) |
| Stage 7b | 390×1048 | Calendar integration | Date/time slot grid + calendar intent |

---

## M1 — Project Scaffolding

- [x] **1.1** Create root `build.gradle.kts` with Kotlin DSL, version catalog (`libs.versions.toml`)
- [x] **1.2** Create `app/build.gradle.kts` with plugins: `com.android.application`, `org.jetbrains.kotlin.android`, `com.apollographql.apollo3`, `com.google.devtools.ksp`
- [x] **1.3** Add dependencies: Compose BOM, M3, Navigation2, Apollo Kotlin (runtime + subscriptions), Koin, Ktor (client + multipart), Room, `androidx.security.crypto`, Coil
- [x] **1.4** Create Clean Architecture package skeleton:
  ```
  ncshack/samba/mizan/
    data/       (remote, local)
    domain/     (model, repository, usecase)
    presentation/ (viewmodel, ui/screens, ui/components)
    di/         (Koin modules)
    navigation/ (NavGraph, routes)
  ```
- [x] **1.5** Configure `AndroidManifest.xml` with permissions (INTERNET, CAMERA, READ/WRITE storage, CALENDAR) and application class
- [x] **1.6** Create `MizanTheme.kt` with M3 `lightColorScheme` seeded from extracted palette (`#e8380d` primary, custom background/surface). Dynamic color disabled.
- [x] **1.7** Create `Shape.kt` — M3 shapes with `RoundedCornerShape(12.dp)` for cards (M3 medium), `RoundedCornerShape(5.dp)` for buttons (matching Figma)
- [x] **1.8** Create `values/strings.xml` (French default) and `values-ar/strings.xml` (Arabic) with app name, common labels
- [x] **1.9** Create `Colors.kt` with the full extracted palette as named constants
- [x] **1.10** Create `Dimens.kt` with extracted spacing values (button radius 5dp, input radius 10dp, card radius 12dp)

## M2 — Navigation Shell & Auth Flow

- [x] **2.1** Create type-safe sealed route classes (`@Serializable`): `Login`, `Register`, `Conversation`, `Documents`, `LawyerProfile(lawyerId)`, `Booking(lawyerId)`
- [x] **2.2** Create `NavGraph.kt` — root NavHost with auth-guard logic: check token in `EncryptedSharedPreferences` → route to `Conversation` or `Login`
- [x] **2.3** Create `MizanApp.kt` — Application class with Koin init
- [x] **2.4** Implement `LoginScreen` matching Figma frame (20:178): `#f6f8fa` background, orange `#e8380d` header area, white input cards with `#eff0f6` 10dp radius stroke, `#e8380d` 5dp radius button
- [x] **2.5** Implement `RegisterScreen` matching Figma frame (20:631): same structure as Login with email/password/confirm fields
- [x] **2.6** Create `AuthViewModel` with `AuthUiState`, `AuthEvent`, `AuthEffect`, intents: `EmailChanged`, `PasswordChanged`, `Login`, `Register`
- [x] **2.7** Create `AuthRepository` + `AuthRepositoryImpl` — login/register GraphQL mutations via Apollo, JWT token storage in `EncryptedSharedPreferences`
- [x] **2.8** Add auth validation UI — inline error below each field (Compose state-driven), `ValidationException` for use case validation
- [x] **2.9** On successful auth: create sessionId, navigate to ConversationScreen, clear backstack
- [x] **2.10** Create use cases: `LoginUseCase`, `RegisterUseCase`, `CheckAuthStatusUseCase` — ViewModel delegates business logic
- [x] **2.11** Create `AuthTextField` shared composable
- [x] **2.12** Wire Koin DI module with use case bindings

## M3 — GraphQL Layer

- [x] **3.1** Create Apollo Client module in `di/` — configure `ApolloClient` with auth interceptor (attaches JWT), WebSocket subscription transport
- [x] **3.2** Download GraphQL schema (or create SDL from PRD): `prompt`, `bookLawyer`, `uploadDocument`, `saveDeadline` mutations; `widgetPush` subscription; `lawyerById`, `lawyerSlots` queries
- [x] **3.3** Create `.graphql` operation files:
  - `PromptMutation.graphql` — `mutation prompt(sessionId: ID!, text: String!, mediaUrl: String)`
  - `BookLawyerMutation.graphql` — `mutation bookLawyer(lawyerId: ID!, slotId: ID!)`
  - `UploadDocumentMutation.graphql` — `mutation uploadDocument(sessionId: ID!, file: Upload!)`
  - `SaveDeadlineMutation.graphql` — `mutation saveDeadline(sessionId: ID!, deadlineId: ID!)`
  - `WidgetPushSubscription.graphql` — `subscription widgetPush(sessionId: ID!) { ... CardDescriptor }`
  - `LawyerByIdQuery.graphql` — `query lawyerById(id: ID!)`
  - `LawyerSlotsQuery.graphql` — `query lawyerSlots(lawyerId: ID!)`
- [x] **3.4** Define `CardDescriptor` data class and `CardType` enum mapping the GraphQL union in `domain/model/`
- [x] **3.5** Create `WidgetPushDataSource` — class that manages the subscription lifecycle (connect, reconnect, emit flow)
- [x] **3.6** Create Ktor module for non-GraphQL HTTP: file upload via multipart POST, file download GET
- [x] **3.7** Wire up Apollo + Ktor modules in Koin

## M4 — ConversationScreen & MVI

- [x] **4.1** Create `ConversationUiState` data class (items list, inputText, isAwaitingResponse, error) and `ConversationIntent` sealed interface (TextChanged, Submit, CardActionTapped, ChipSelected)
- [x] **4.2** Create `ConversationViewModel` — processes intents, calls `prompt` mutation (ack-only), subscribes to `widgetPush`, appends `CardGroup` to items on each push
- [x] **4.3** Build `ConversationScreen` composable matching Figma Stage 1 (50:1714): TopAppBar with "Lexify" `#e8380d` logo, LazyColumn, cards with `#ffffff` on `#f7f7f7` background
- [x] **4.4** Build `MessageBubble` composable — user message bubble with right-alignment matching Figma Stage 2
- [x] **4.5** Build `TypingIndicator` — M3 shimmer/dot animation when `isAwaitingResponse = true`
- [x] **4.6** Build `MessageInput` composable — TextField + Send button with disabled state during loading. Matches Figma input bar styling
- [x] **4.7** Wire subscription from ViewModel: on session start → subscribe → on each `CardDescriptor` → append to items → set `isAwaitingResponse = false`
- [x] **4.8** Create `CardGroup` composable — renders a list of `CardDescriptor` items via `CardRenderer` inside a Column in the LazyColumn

## M5 — Card System

- [x] **5.1** Create `CardRenderer` composable — `when(card.type)` dispatches to per-type card composables
- [x] **5.2** Create shared M3 `Card` anatomy: Header (icon + title + badge), Body, Footer (FilledButton + OutlinedButton)
- [x] **5.3** Implement `ClarifyingChipsCard` matching Figma Stage 3 — FlowRow of FilterChip, single-select, disabled on selection
- [x] **5.4** Implement `ContextCard` — ElevatedCard with reduced emphasis (tonal surface), dismissible via close icon or swipe, source citation + disclaimer lines
- [x] **5.5** Implement `LawyerCarouselCard` matching Figma Stage 7 — horizontal LazyRow of lawyer mini-cards: avatar, name, specialty, wilaya, rating. Footer: "Book now" + "View profile"
- [x] **5.6** Implement `BookingCard` — inline date/time slot grid from payload, "Confirm booking" button → `bookLawyer` mutation, success state, "Add to calendar" follow-on chip
- [x] **5.7** Implement `SubmitDocumentCard` matching Figma Stage 6 — description, "Upload file" button (GetContent), "Take photo" button (TakePicture), upload progress, success state
- [x] **5.8** Implement `DownloadFileCard` — document name + description, "Download" button → Ktor GET + MediaStore save, "Preview" button, snackbar on success
- [x] **5.9** Implement `DeadlineCard` — deadline label + date + context, "Add to calendar" button → CalendarContract.Events intent, "Added" success state on return

## M6 — Documents & Local Persistence

- [ ] **6.1** Create Room database: `AppDatabase`, `DocumentEntity`, `DocumentDao`
- [ ] **6.2** Create `DocumentsRepository` — save on download success, query all, delete
- [ ] **6.3** Implement file download logic in Ktor: GET request → save to Downloads via `MediaStore` (API 29+) or `Environment.getExternalStoragePublicDirectory` (below)
- [ ] **6.4** Create `DocumentsUiState` and `DocumentsViewModel` — load from Room, expose list
- [ ] **6.5** Implement `DocumentsScreen` — LazyColumn of file rows (icon, name, date, "Open" button), empty state, loading state
- [ ] **6.6** Wire Koin modules for Room, DAO, Repository

## M7 — Lawyer & Booking Screens

- [ ] **7.1** Create `LawyerProfileViewModel` — fetches `lawyerById`
- [ ] **7.2** Implement `LawyerProfileScreen` — avatar, name, specialty, wilaya, rating, bio, languages, fee range, "Book now" button
- [ ] **7.3** Create `BookingViewModel` — fetches `lawyerSlots`, processes `bookLawyer` mutation
- [ ] **7.4** Implement `BookingScreen` — date selector, time slot grid matching Stage 7b Figma, confirm button, success state, "Add to calendar" option
- [ ] **7.5** Create `LawyerRepository` — wraps Apollo queries for lawyer data
- [ ] **7.6** Wire navigation from LawyerCarouselCard → LawyerProfileScreen, BookingScreen

## M7b — UI Polishing (Figma Compliance)

- [x] **7b.1** Rename app from "Mizan" to "Lexify" (strings, manifest, screens)
- [x] **7b.2** Fix `OnSurfaceVariant` color contrast: `#8C8C8C` → `#6B6B6B` (5.7:1 ratio, WCAG AA compliant)
- [x] **7b.3** Fix Login Screen: 44dp status bar spacer, 206dp header with square edges, input fields wrapped in white Card (radius=10), Google login button placeholder, "Or log in with" divider, "Forgotten password?" link, 34dp home indicator
- [x] **7b.4** Fix Register Screen: same structural changes as Login
- [x] **7b.5** Fix Conversation Screen: CenterAlignedTopAppBar (64dp, `#fbf9f9`, Lexify logo), `#ffffff` background, MessageInput in rounded top-corner container, 34dp home indicator
- [x] **7b.6** Add Apollo `LoggingInterceptor` to ApolloClient builder
- [x] **7b.7** Add string resources for new UI elements (ask_legal_question)

## M8 — Permissions, Error Handling, Localization & RTL

- [ ] **8.1** Create `PermissionUtils.kt` — reusable `rememberLauncherForActivityResult` helpers for CAMERA, READ_MEDIA_IMAGES, READ/WRITE_CALENDAR, WRITE_EXTERNAL_STORAGE
- [ ] **8.2** Add rationale dialog before each permission request
- [ ] **8.3** Handle permanent denial → show settings redirect dialog with `Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)`
- [ ] **8.4** Create global error handling — network errors surface as `SideEffect` channel → Snackbar via `SnackbarHostState`
- [ ] **8.5** Handle WebSocket disconnects — ViewModel emits snackbar + retry subscription
- [ ] **8.6** "No internet connection" Snackbar on connectivity loss (use ConnectivityManager)
- [ ] **8.7** Create `values/strings.xml` (French default) — all user-facing strings
- [ ] **8.8** Create `values-ar/strings.xml` (Arabic) — translated strings
- [ ] **8.9** Set up `CompositionLocalProvider` in `MizanApp.kt` for `LayoutDirection` based on locale
- [ ] **8.10** Audit all layouts: replace `left`/`right` with `start`/`end`, verify `FlowRow` + RTL behavior
- [ ] **8.11** Configure Arabic typography override in `Type.kt` — Cairo/Tajawal font, increased `lineHeight`
