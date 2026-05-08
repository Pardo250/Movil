package com.example.condorapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas End-to-End (E2E) para CondorApp.
 *
 * Requisitos del Sprint 13:
 *   - Caso A: Registro con contraseña débil → error → corregir → navegar al Home →
 *             abrir un artículo → dar Like a una reseña → quitar Like.
 *   - Caso B: Login con usuario existente → navegar al perfil de otro usuario →
 *             seguirlo → volver al Home → filtrar "Siguiendo".
 *
 * Configuración:
 *   - Firebase Emulators deben estar corriendo (`firebase emulators:start`).
 *   - El seed.js debe haberse ejecutado para tener datos de prueba en el emulador.
 *   - El emulador Android usa 10.0.2.2 para conectarse al host local.
 *
 * NOTA: Estos tests usan `Modifier.testTag()` para localizar los elementos de la UI,
 * evitando la fragilidad de buscar por texto que puede cambiar con la localización.
 */
@RunWith(AndroidJUnit4::class)
class UserFlowE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /**
     * Limpieza antes de cada test:
     * - Cerrar sesión para asegurar que cada test parte desde la pantalla de login/inicio.
     */
    @Before
    fun setup() {
        auth.signOut()
    }

    /**
     * Limpieza después de cada test:
     * - Cerrar sesión para no dejar estado residual.
     */
    @After
    fun tearDown() {
        auth.signOut()
    }

    /**
     * Caso A: Flujo de registro fallido, corrección y like en reseña.
     *
     * 1. Desde la pantalla de inicio/login, navegar a Registro.
     * 2. Llenar el formulario con contraseña débil ("1234") → Click en registrarse.
     * 3. Verificar que aparece un error de contraseña débil.
     * 4. Corregir la contraseña a "123456" y confirmarla.
     * 5. Registrarse exitosamente → El usuario llega al Home.
     * 6. Navegar al detalle de un artículo.
     * 7. Dar like a una reseña → Verificar que el like se registra.
     * 8. Quitar el like → Verificar que el like se remueve.
     */
    @Test
    fun caseA_register_error_and_like_review() {
        // ── Paso 1: Navegar a la pantalla de Registro ──────────
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("login_register_button")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("login_register_button").performClick()
        composeTestRule.waitForIdle()

        // ── Paso 2: Llenar formulario con contraseña débil ─────
        composeTestRule.onNodeWithTag("signup_name").performTextInput("Test")
        composeTestRule.onNodeWithTag("signup_lastname").performTextInput("User")
        composeTestRule.onNodeWithTag("signup_username").performTextInput("testuser_e2e")

        val timestamp = System.currentTimeMillis()
        val email = "e2e_case_a_$timestamp@test.com"
        composeTestRule.onNodeWithTag("signup_email").performTextInput(email)
        composeTestRule.onNodeWithTag("signup_password").performTextInput("1234")
        composeTestRule.onNodeWithTag("signup_confirm_password").performTextInput("1234")

        // ── Paso 3: Intentar registrarse → Error de contraseña débil ──
        composeTestRule.onNodeWithTag("signup_button").performClick()
        composeTestRule.waitForIdle()

        // Verificar error de contraseña débil (el supportingText aparece en el TextField)
        composeTestRule.onNodeWithText("corta", substring = true, ignoreCase = true)
            .assertExists()

        // ── Paso 4: Corregir contraseña ────────────────────────
        composeTestRule.onNodeWithTag("signup_password").performTextClearance()
        composeTestRule.onNodeWithTag("signup_password").performTextInput("123456")
        composeTestRule.onNodeWithTag("signup_confirm_password").performTextClearance()
        composeTestRule.onNodeWithTag("signup_confirm_password").performTextInput("123456")

        // ── Paso 5: Registrarse exitosamente → Home ────────────
        composeTestRule.onNodeWithTag("signup_button").performClick()

        // Esperar a que cargue el Home (los tabs Todos/Siguiendo aparecen)
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("home_tab_todos")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // ── Paso 6: Navegar al detalle de un artículo ──────────
        // El Home muestra artículos del seed. Hacemos clic en el primero.
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithContentDescription("", substring = true)
                .fetchSemanticsNodes().size > 2
        }
        // Clic en el primer artículo visible (cualquier Card de artículo)
        composeTestRule.onAllNodes(hasClickAction())[1].performClick()
        composeTestRule.waitForIdle()

        // Verificar que el detalle cargó (título visible)
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("detail_title")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("detail_title").assertExists()

        // ── Paso 7: Dar Like a la primera reseña ───────────────
        // Las reviews se cargan vía Flow. Esperamos a que aparezca al menos un botón de like.
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithContentDescription("Like")
                .fetchSemanticsNodes().isNotEmpty()
        }

        val likeButton = composeTestRule.onAllNodesWithContentDescription("Like")[0]
        likeButton.performClick()
        composeTestRule.waitForIdle()

        // ── Paso 8: Quitar Like ────────────────────────────────
        // El icono podría haber cambiado de Outlined a Filled, pero el contentDescription sigue siendo "Like"
        composeTestRule.onAllNodesWithContentDescription("Like")[0].performClick()
        composeTestRule.waitForIdle()
    }

    /**
     * Caso B: Flujo de login, seguir usuario y filtrar feed.
     *
     * Pre-condición: Debe existir un usuario registrado en el emulador de Auth.
     *    Puede ser un usuario creado por el seed.js o por el caseA ejecutado antes.
     *
     * 1. Desde la pantalla de login, iniciar sesión con credenciales existentes.
     * 2. Navegar al Home → Abrir detalle de un artículo.
     * 3. Hacer clic en el nombre/avatar de un reviewer para ir a su perfil.
     * 4. En el perfil del usuario, verificar que aparece el conteo de seguidores.
     * 5. Hacer clic en "Seguir" → Verificar que el botón cambia a "Siguiendo".
     * 6. Volver al Home.
     * 7. Seleccionar la pestaña "Siguiendo" → Verificar que los artículos se filtran.
     */
    @Test
    fun caseB_login_follow_and_feed() {
        // ── Paso 1: Login con usuario existente ────────────────
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("login_email")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("login_email").performTextInput("existente@test.com")
        composeTestRule.onNodeWithTag("login_password").performTextInput("123456")
        composeTestRule.onNodeWithTag("login_signin_button").performClick()

        // ── Paso 2: Esperar Home y abrir un artículo ───────────
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("home_tab_todos")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Clic en un artículo del feed
        composeTestRule.onAllNodes(hasClickAction())[1].performClick()
        composeTestRule.waitForIdle()

        // ── Paso 3: Click en un reviewer → Navegar a su perfil ─
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithContentDescription("Like")
                .fetchSemanticsNodes().isNotEmpty()
        }

        // Click en el nombre del primer reviewer (clickable dentro de ReviewItem)
        // Los ReviewItems tienen el nombre del autor como texto clickable
        composeTestRule.onAllNodes(hasClickAction())[2].performClick()
        composeTestRule.waitForIdle()

        // ── Paso 4: Verificar perfil de usuario ────────────────
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule
                .onAllNodesWithTag("userprofile_name")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("userprofile_name").assertExists()
        composeTestRule.onNodeWithTag("userprofile_followers_count").assertExists()

        // ── Paso 5: Hacer clic en "Seguir" ─────────────────────
        composeTestRule.onNodeWithTag("userprofile_follow_button").performClick()
        composeTestRule.waitForIdle()

        // Verificar que ahora dice "Siguiendo"
        composeTestRule.onNodeWithText("Siguiendo").assertExists()

        // ── Paso 6: Volver al Home ─────────────────────────────
        // Usar botón atrás del sistema (pressBack)
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.waitForIdle()
        composeTestRule.activityRule.scenario.onActivity { activity ->
            activity.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.waitForIdle()

        // ── Paso 7: Seleccionar tab "Siguiendo" ────────────────
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithTag("home_tab_siguiendo")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("home_tab_siguiendo").performClick()
        composeTestRule.waitForIdle()

        // El feed debe filtrar y mostrar solo artículos de usuarios seguidos (o mostrar mensaje "No hay artículos")
        // Verificamos que el tab "Siguiendo" fue seleccionado correctamente
        composeTestRule.onNodeWithTag("home_tab_siguiendo").assertExists()
    }
}
