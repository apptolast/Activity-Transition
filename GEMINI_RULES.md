# KMP & MVI ARCHITECTURE RULES FOR GEMINI (EXTENDED)

You are an expert Senior Kotlin Multiplatform (KMP) Developer. Your mission is to generate code following a strict MVI pattern and Clean Architecture, ensuring compatibility across Android and iOS, including platform-specific Android components.

## 1. TECH STACK (STRICT ADHERENCE)
* **Language:** Kotlin Multiplatform (CommonMain focused).
* **Architecture:** MVI (Model-View-Intent).
* **DI:** Koin (KMP compatible).
* **Networking:** Ktor Client.
* **Firebase:** GitLive Firebase KMP SDK.
* **Concurrency:** Coroutines & Flows (StateFlow for State, SharedFlow for Effects).

## 2. PROJECT STRUCTURE & LAYERS

### A. DOMAIN LAYER (`commonMain/.../domain`)
* **Repository Interfaces:** Define contracts for data.
* **Service Interfaces:** Define contracts for platform features (e.g., `NotificationService`, `BackgroundService`).
* **Use Cases:** Business logic that triggers Repository or Service actions.

### B. DATA & PLATFORM LAYER
* **Data Sources:** Ktor/Firebase implementations in `commonMain`.
* **Platform Impl (`androidMain`):** Real implementations of `NotificationService`, `BroadcastReceivers`, and `Services`.

### C. PRESENTATION LAYER (MVI - `commonMain`)
* **State/Intent/Effect:** Standard MVI flow using StateFlow and SharedFlow.

## 3. ANDROID SPECIFIC COMPONENTS (CLEAN INTEGRATION)

### SERVICES & BROADCAST RECEIVERS
* **Abstraction:** Always define an interface in `commonMain` to trigger or stop services/receivers from the ViewModel/UseCase.
* **Injection:** Inject the Android `Context` via Koin in `androidMain` to initialize these components.
* **Lifecycle:** Ensure `Services` are declared in `AndroidManifest.xml` and follow modern Android background limits (Foreground Services with Notifications).

### NOTIFICATIONS
* **Notification Channels:** Create and manage channels in `androidMain`.
* **Flow:** UseCase -> NotificationService (Interface) -> AndroidNotificationServiceImpl (Actual Notification).

## 4. CODING STANDARDS

* **State Management:** Expose `StateFlow` as `val state` and `SharedFlow` as `val effect`. Use `update { ... }`.
* **DI with Koin:** Use `factoryOf`, `singleOf`, and `viewModelOf`.
* **Error Handling:** Use a Result wrapper or specific Error effects.
* **KMP Logic:** Keep `commonMain` free of `android.*` imports. Use `expect/actual` or interface injection for platform-specifics.

## 5. EXAMPLE: ANDROID SERVICE INJECTION
```kotlin
// commonMain
interface MyBackgroundService {
    fun start()
    fun stop()
}

// androidMain
class AndroidBackgroundService(private val context: Context) : MyBackgroundService {
    override fun start() {
        val intent = Intent(context, RealAndroidService::class.java)
        context.startService(intent)
    }
    // ...
}