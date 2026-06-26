# Mizan — AI Legal Companion for Algerian Citizens

> **Hackathon:** NCS x Samba | **Track:** Android App  
> **Theme:** Empowering Algerian citizens with AI-driven legal assistance

---

## Table of Contents

- [App Idea](#app-idea)
- [Architecture & Patterns](#architecture--patterns)
- [UI / UX](#ui--ux)
- [Libraries & Tooling](#libraries--tooling)
- [Constraints & Requirements](#constraints--requirements)
- [Project Structure](#project-structure)
- [Build Guide](#build-guide)
- [Team](#team)

---

## App Idea

**Mizan** (ميزان — *balance / justice* in Arabic) is an Android application that provides Algerian citizens with **free, AI-powered legal guidance** in Arabic and French. Users describe their legal situation in natural language; Mizan responds with structured cards containing legal analysis, document templates, lawyer recommendations, deadline reminders, and booking options — all tailored to Algerian law.

**Why Mizan?** Legal aid in Algeria is scarce, expensive, and inaccessible to most citizens. Mizan bridges this gap by combining a fine-tuned LLM (the *Ariane* model) with a rich, interactive UI — no lawyer needed for preliminary advice.

### Core Features (implemented M1–M5)

| Feature | Description |
|---|---|
| **Conversational AI** | Describe your situation; get structured legal answers |
| **Legal Analysis cards** | Statutes, articles, and citations from Algerian law |
| **Lawyer Carousel** | Browse recommended lawyers by specialty & rating |
| **Booking** | Book consultation slots with a lawyer |
| **Document Submission** | Upload evidence / documents for review |
| **Deadline Tracking** | Save critical legal deadlines to your calendar |
| **Knowledge Base** | Algerian legal information with confidence scores |
| **Action Required** | Guided multi-step legal workflows |
| **Multi-session** | Sidebar with history of all past conversations |
| **RTL / Arabic** | Full Arabic UI with bundled Cairo font |
| **Auth** | Email + password with JWT token storage |

---

## Architecture & Patterns

### Clean Architecture + MVI

The project follows **Clean Architecture** with three layers, strict dependency rules, and an **MVI (Model-View-Intent)** pattern in the presentation layer.

```
┌─────────────────────────────────────────────────┐
│                   Presentation                   │
│  (Composables / ViewModel / UiState / Intent)    │
├─────────────────────────────────────────────────┤
│                     Domain                       │
│    (Use Cases / Repository Interfaces / Models)  │
├─────────────────────────────────────────────────┤
│                      Data                        │
│   (Apollo GraphQL / Ktor HTTP / Room DB / Repos) │
└─────────────────────────────────────────────────┘
```

**Rules enforced:**
- UI imports only interfaces (repositories), never their implementations.
- ViewModels delegate all business logic to `suspend` use cases.
- Use cases throw exceptions; ViewModels catch them in `viewModelScope.launch`.
- No `runCatching` inside use cases — exceptions propagate naturally.

### MVI (per screen)

Each screen has three primitives:

```
UiState (data class)  ←  ViewModel  →  Intent (sealed interface)
                             ↓
                       SideEffect (Channel)
                             ↓
                     Snackbar / Navigation
```

- **UiState** — single source of truth exposed as `StateFlow`.
- **Intent** — user actions (`TextChanged`, `Submit`, `SelectSession`, etc.).
- **ViewModel** — processes intents, updates state, emits side effects.
- **SideEffect** — one-shot events (errors, navigation) via `Channel`.

### Repository Pattern

```
ViewModel → UseCase → Repository (interface) ← RepositoryImpl
                                                    ↓
                                         ┌─────────┼─────────┐
                                    ApolloClient  Ktor     Room
```

### GraphQL (Apollo Kotlin 5)

- **Queries** — fetching data (lawyers, sessions, prompt history).
- **Mutations** — writing data (prompt, book, upload). Return **ack-only**.
- **Subscriptions** — real-time `widgetPush(sessionId)` streams `CardDescriptor[]`.

Mutations produce an immediate ack via the mutation response, while real-time updates arrive via the subscription. The `widgetPush` subscription is the single source of truth for UI updates.

### Card System — the core UI primitive

All AI responses arrive as `CardDescriptor` objects through the `widgetPush` subscription (and inline via mutation responses). A `CardRenderer` dispatches on `cardType` (snake_case string) to per-type composables.

```kotlin
@Serializable
data class CardDescriptor(
    val id: String,
    val cardType: String,    // "text", "lawyer_carousel", "info", "deadline"...
    val payload: String,     // JSON string
)
```

| Card Type | Purpose |
|---|---|
| `text` | Plain AI message (rendered as a chat bubble) |
| `info` | Informational block with title, source, text |
| `legal_analysis` | Statute breakdown with sections & citations |
| `knowledge_result` | Factual answer from knowledge base |
| `action_required` | Multi-step workflow with action buttons |
| `lawyer_carousel` | Horizontal carousel of lawyer mini-cards |
| `deadline` | Calendar-savable deadline reminder |
| `booking` | Slot picker + booking confirmation |
| `submit_document` | Upload button for evidence / documents |
| `download_file` | Download link for generated documents |
| `context` | Dismissible context/explanation banner |
| `step` | Clarifying question with response chips |

Each card composable is wrapped in a `SharedCard` layout that provides a **dynamic Material 3 color scheme** per card type using the `MaterialKolor` library.

---

## UI / UX

- **Jetpack Compose + Material 3** — fully declarative UI.
- **Dynamic color disabled** — fixed seed palette from Figma (`#E8380D` primary).
- **Custom theme** — `DynamicMaterialTheme` from `materialkolor` generating M3 schemes per card type.
- **RTL support** — Arabic layout with `Start`/`End` semantics (no `Left`/`Right`).
- **Fonts** — Latin: `Roboto` (M3 default), Arabic: `Cairo` (bundled).
- **Splash screen** — animated Material 3 splash via `androidx.core.splashscreen`.
- **Figma compliance** — all dimensions, colors, shadows, and typography match the Figma prototype.
- **Shimmer skeleton** — card-shaped shimmer animation while waiting for AI response.

---

## Libraries & Tooling

| Category | Library | Purpose |
|---|---|---|
| **GraphQL** | Apollo Kotlin 5 | Queries, mutations, WebSocket subscriptions |
| **HTTP** | Ktor client | File upload/download (non-GraphQL) |
| **DI** | Koin | Dependency injection (modules, ViewModel factory) |
| **Local DB** | Room | Document metadata persistence |
| **Auth** | AndroidX Security Crypto | `EncryptedSharedPreferences` for JWT tokens |
| **Navigation** | Navigation2 (type-safe) | `@Serializable` route objects |
| **Serialization** | Kotlinx Serialization | JSON parsing, route serialization |
| **Images** | Coil | Async image loading for lawyer photos |
| **Theming** | Material Kolor, KMPalette | Dynamic M3 color schemes per card type |
| **Animations** | Compose Animation | Shimmer skeleton, expressive carousels |
| **Build** | Gradle KTS, KSP, Apollo codegen | Code generation, compilation, minification |

---

## Constraints & Requirements

### Android
- **minSdk**: 29 (Android 10)
- **targetSdk**: 36 (Android 16)
- **App name**: Mizan
- **Package**: `ncshack.samba.mizan`

### Backend
- **GraphQL endpoint**: configured in `ApiService.kt`
- **Schema**: `app/src/main/graphql/schema.graphqls`
- All mutation responses are ack-only; real-time updates arrive via `widgetPush` subscription
- `SessionId` is created post-auth and sent with every mutation

### Limitations (known)
- **No offline mode** — requires network for all operations. Disconnection shows a Snackbar.
- **No anonymous use** — email + password auth required.
- **Single language per session** — language is set at session creation.

---

## Project Structure

```
app/
├── build.gradle.kts
├── proguard-rules.pro
├── src/
│   ├── main/
│   │   ├── AndroidManifest.xml
│   │   ├── graphql/
│   │   │   ├── schema.graphqls
│   │   │   └── ncshack/samba/mizan/operations/
│   │   │       ├── Login.graphql
│   │   │       ├── Register.graphql
│   │   │       ├── Prompt.graphql
│   │   │       ├── PromptsBySession.graphql
│   │   │       ├── StartSession.graphql
│   │   │       ├── MySessions.graphql
│   │   │       ├── WidgetPush.graphql
│   │   │       ├── BookLawyer.graphql
│   │   │       ├── LawyerById.graphql
│   │   │       ├── SaveDeadline.graphql
│   │   │       └── UploadDocument.graphql
│   │   ├── java/ncshack/samba/mizan/
│   │   │   ├── LexifyApp.kt
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── remote/          # Apollo data sources, Ktor API, AuthInterceptor
│   │   │   │   └── local/           # Room DAOs, database
│   │   │   ├── di/
│   │   │   │   └── AppModule.kt     # Koin module
│   │   │   ├── domain/
│   │   │   │   ├── model/           # CardDescriptor, Session, LocalDocument
│   │   │   │   ├── repository/      # AuthRepository, SessionRepository, DocumentRepository
│   │   │   │   └── usecase/         # Login, Register, Prompt, PromptsBySession, etc.
│   │   │   ├── navigation/
│   │   │   │   ├── Routes.kt        # @Serializable route objects
│   │   │   │   └── NavGraph.kt      # Auth-guarded NavHost
│   │   │   ├── presentation/
│   │   │   │   ├── viewmodel/       # ConversationViewModel, AuthViewModel
│   │   │   │   └── ui/screens/
│   │   │   │       ├── auth/        # Login / Register
│   │   │   │       └── conversation/ # Chat, cards, sidebar
│   │   │   └── ui/
│   │   │       ├── theme/           # Color, Theme, Type, Dimens, Shape
│   │   │       └── preview/         # Previews.kt (all card types)
│   │   └── res/
│   │       ├── values/              # strings.xml (French default)
│   │       ├── values-ar/           # strings.xml (Arabic)
│   │       └── font/                # Cairo (Arabic font)
│   └── test/
```

---

## Build Guide

### Prerequisites

- **Android Studio** Ladybug (2024.2) or newer
- **JDK** 21+
- **Android SDK** 36 (compileSdk)
- **Gradle** 8.x (wrapper included)

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/<org>/mizan-android.git
cd mizan-android

# 2. Build debug APK (installable)
./gradlew assembleDebug

# 3. Build release APK (with ProGuard minification)
./gradlew assembleRelease

# 4. Locate the APK
#    app/build/outputs/apk/debug/app-debug.apk
#    app/build/outputs/apk/release/app-release.apk

# 5. Run on device / emulator
./gradlew installDebug
```

### Build Variants

| Variant | Minification | ProGuard | Use Case |
|---|---|---|---|
| `debug` | ❌ | ❌ | Development |
| `release` | ✅ | ✅ | Distribution |

### ProGuard

See `app/proguard-rules.pro` for the full set of keep rules covering:
- Navigation2 `@Serializable` route classes
- Apollo Kotlin generated operation & type classes
- Kotlinx Serialization serializers
- Koin DI components
- Room DAOs and entities
- Coil, Ktor, Material Kolor, AndroidX Security

### Environment Variables / Secrets

The GraphQL endpoint URL is defined in `ApiService.kt` (companion object). No API keys are hardcoded — JWT tokens are obtained through the auth flow and stored in `EncryptedSharedPreferences`.

---

## Team

Built with ❤️ for the **NCS x Samba Hackathon**.
