# 🦅 CondorApp

**Descubre el realismo mágico de Colombia** — Aplicación móvil Android para explorar destinos turísticos colombianos, compartir reseñas y conectar con otros viajeros.

---

## 📌 Descripción

**CondorApp** es una aplicación móvil nativa enfocada en la exploración de destinos, cultura y experiencias en la región andina y más allá. Desarrollada con **Kotlin** y **Jetpack Compose**, implementando la arquitectura **MVVM (Model-View-ViewModel)** con buenas prácticas de gestión de estado.

Proyecto desarrollado como parte del curso de Computación Móvil, aplicando prácticas modernas en desarrollo Android.

---

## ✨ Características

| Funcionalidad | Descripción |
|----------------|-------------|
| 🏠 **Home Feed** | Posts de destinos turísticos con imágenes y datos de viajeros |
| 🔍 **Explorar** | Búsqueda y filtrado por categorías (Paisaje, Playas, Cultural, Hoteles) |
| 👤 **Perfil** | Gestión de perfil con edición de datos y galería de fotos |
| ⭐ **Reseñas** | Creación y visualización con calificación por estrellas |
| 🔔 **Notificaciones** | Centro de notificaciones con filtrado por tabs |
| 🔐 **Autenticación** | Login y Registro con validación de formularios |
| 🌙 **Modo Oscuro** | Soporte completo de tema claro/oscuro (Material Design 3) |

---

## 🏗️ Arquitectura MVVM

La aplicación implementa la arquitectura **MVVM** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────────┐
│                    UI Layer                  │
│  Screen (Composable) ← ViewModel ← UiState  │
├─────────────────────────────────────────────┤
│                  Data Layer                  │
│           Repository (Local Data)            │
├─────────────────────────────────────────────┤
│                 Model Layer                  │
│         Data Classes (Post, Review...)       │
└─────────────────────────────────────────────┘
```

### Patrón por pantalla

Cada pantalla sigue una estructura consistente:

- **`*UiState.kt`** — Data class inmutable que representa el estado del UI
- **`*ViewModel.kt`** — Expone `StateFlow<UiState>` y gestiona la lógica de negocio
- **`*Screen.kt`** — Composables stateless (`Route` + `Content`) con state hoisting

```kotlin
// Ejemplo: HomeViewModel.kt
class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { loadPosts() }

    private fun loadPosts() {
        val posts = PostRepository.getPosts()
        _uiState.update { it.copy(posts = posts) }
    }
}

// Ejemplo: HomeScreenRoute (collectAsStateWithLifecycle)
@Composable
fun HomeScreenRoute(viewModel: HomeViewModel = viewModel(), ...) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreenContent(state = uiState, ...)
}
```

---

## 🛠️ Tecnologías y Dependencias

| Tecnología | Descripción |
|------------|-------------|
| **Kotlin** | Lenguaje principal |
| **Jetpack Compose** | Framework declarativo de UI |
| **Material Design 3** | Componentes y sistema de diseño |
| **Navigation Compose** | `2.7.7` — Navegación entre pantallas |
| **ViewModel** | `lifecycle-viewmodel-compose:2.6.1` |
| **StateFlow** | `MutableStateFlow` + `collectAsStateWithLifecycle` |
| **Gradle KTS** | Build system |

---

## 📂 Estructura del Proyecto

```
app/src/main/java/com/example/condorapp/
├── MainActivity.kt                    # Activity principal (Scaffold único)
├── data/
│   ├── Post.kt                        # Modelo de publicación
│   ├── FeedPlace.kt                   # Modelo de lugar
│   ├── Review.kt                      # Modelo de reseña
│   ├── UserProfile.kt                 # Modelo de perfil
│   ├── Notification.kt                # Modelo de notificación
│   └── local/
│       ├── PostRepository.kt          # Datos simulados de posts
│       ├── FeedRepository.kt          # Datos simulados de lugares
│       ├── ReviewRepository.kt        # Datos simulados de reseñas
│       ├── UserProfileRepository.kt   # Datos simulados de perfil
│       └── NotificationRepository.kt  # Datos simulados de notificaciones
└── ui/
    ├── inicio/          # Pantalla de bienvenida (UiState + Screen)
    ├── login/           # Login (ViewModel + UiState + Screen)
    ├── signup/          # Registro (ViewModel + UiState + Screen)
    ├── home/            # Feed principal (ViewModel + UiState + Screen)
    ├── feed/            # Exploración (ViewModel + UiState + Screen)
    ├── profile/         # Perfil (ViewModel + UiState + Screen)
    ├── notifications/   # Notificaciones (ViewModel + UiState + Screen)
    ├── detail/          # Detalle destino (ViewModel + UiState + Screen)
    ├── review/          # Detalle reseña (ViewModel + UiState + Screen)
    ├── editprofile/     # Editar perfil (ViewModel + UiState + Screen)
    ├── createreview/    # Crear reseña (ViewModel + UiState + Screen)
    ├── navigation/      # Routes, AppNavigation, BottomNavigationBar
    └── theme/           # Color.kt, Theme.kt, Type.kt
```

---

## 📍 Navegación

La app implementa navegación centralizada con 11 pantallas:

```
Inicio → Login → Home ←→ Explore ←→ Profile
          ↓        ↓         ↓          ↓
        SignUp   Details   Details   EditProfile
                   ↓
              CreateReview
                   ↓
                Review
```

Pantallas principales (Home, Explore, Profile) incluyen barra de navegación inferior flotante.

---

## 🎨 Paleta de Colores

Inspirada en la naturaleza colombiana, con soporte completo para modo claro y oscuro:

| Color | Hex | Uso |
|-------|-----|-----|
| 🟢 Condor Green | `#4F7336` | Primario: botones, headers |
| 🟤 Condor Brown | `#B08968` | Secundario |
| 🟢 Soft Green | `#8FA77B` | Chips, elementos secundarios |
| ⬜ Background | `#F5F7F1` | Fondo (modo claro) |
| ⬛ Dark BG | `#1A1C18` | Fondo (modo oscuro) |

---

## 🚀 Instalación y Ejecución

### Prerrequisitos
- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK 11+**
- **Android SDK** API 24+ (min) / 36 (target)

### Pasos

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Pardo250/Movil.git
   ```

2. Abrir el proyecto en **Android Studio** (carpeta `Movil/`)

3. Sincronizar Gradle

4. Ejecutar en emulador o dispositivo físico (▶️ Run)

---

## 🔮 Mejoras Futuras

- Integración con API de destinos turísticos
- Persistencia de datos (Room / Firebase)
- Sistema de autenticación real (Firebase Auth)
- Tests unitarios para ViewModels

---

## 👨‍💻 Autores

**Andrés Felipe Pinzón Márquez**
**Juan Sebastian Urbano**
**Juan Diego Pardo**

Estudiantes de Ingeniería
GitHub: [github.com/Pardo250](https://github.com/Pardo250)

---

## 📄 Licencia

Proyecto desarrollado con fines académicos.

---

> *CondorApp — Explora el realismo mágico* 🇨🇴
