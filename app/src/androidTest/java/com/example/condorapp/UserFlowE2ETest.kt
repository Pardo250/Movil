package com.example.condorapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserFlowE2ETest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun caseA_register_error_and_like_review() {
        // Asumiendo que la app inicia en Splash y va al Inicio (donde hay un botón Registrarse o Login)
        // O inicia directo en Login.
        // Hacemos que navegue a Registro:
        composeTestRule.onNodeWithText("Registrarse", ignoreCase = true, substring = true)
            .performClick()

        // Llenar datos, password "1234"
        composeTestRule.onNodeWithText("Nombre").performTextInput("Usuario Nuevo")
        composeTestRule.onNodeWithText("Email").performTextInput("nuevo@test.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("1234")
        
        // Click en botón de registro final
        composeTestRule.onNodeWithText("Crear Cuenta", ignoreCase = true, substring = true)
            .performClick()

        // Verificar mensaje de error (esto asume que el ViewModel arroja un error en un Snackbar/Text)
        composeTestRule.onNodeWithText("débil", substring = true, ignoreCase = true).assertExists()
        // O si tu app valida localmente:
        // composeTestRule.onNodeWithText("6 caracteres", substring = true).assertExists()

        // Corregir contraseña
        composeTestRule.onNodeWithText("Contraseña").performTextReplacement("123456")
        composeTestRule.onNodeWithText("Crear Cuenta", ignoreCase = true, substring = true)
            .performClick()

        // Esperar que cargue el Home y se muestre la primera publicación
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Valle del Cocora", substring = true).fetchSemanticsNodes().isNotEmpty()
        }

        // Click en la primera publicación
        composeTestRule.onAllNodesWithText("Valle del Cocora", substring = true)[0].performClick()

        // Verificar info del detalle
        composeTestRule.onNodeWithText("Valle del Cocora", substring = true).assertExists()

        // Navegar a Reviews (asumiendo que hay un botón o pestaña)
        composeTestRule.onNodeWithText("Ver Reseñas", substring = true, ignoreCase = true).performClick()

        // Dar like al primer comentario (buscar el icono de Like)
        // Asume contentDescription "Me gusta" o "Like"
        val likeButton = composeTestRule.onAllNodesWithContentDescription("Like", ignoreCase = true)[0]
        likeButton.performClick()

        // Verificar que sume likes (se asume que el texto muestra "1")
        composeTestRule.onNodeWithText("1", substring = true).assertExists()

        // Quitar el Like
        likeButton.performClick()

        // Verificar que disminuya
        composeTestRule.onNodeWithText("0", substring = true).assertExists()
    }

    @Test
    fun caseB_login_follow_and_feed() {
        // Iniciar en Login (Si hay botón de Ya tienes cuenta en la primera pantalla)
        composeTestRule.onNodeWithText("Iniciar", substring = true, ignoreCase = true).performClick()

        // Llenar datos
        composeTestRule.onNodeWithText("Email").performTextInput("existente@test.com")
        composeTestRule.onNodeWithText("Contraseña").performTextInput("123456")
        composeTestRule.onNodeWithText("Entrar", substring = true, ignoreCase = true).performClick()

        // Ir al perfil de otro usuario (podría ser haciendo clic en un usuario en una review o en el Feed)
        // Supongamos que en el detalle de un post damos clic al nombre del autor
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Autor", substring = true).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onAllNodesWithText("Autor", substring = true)[0].performClick()

        // Verificar info de usuario
        composeTestRule.onNodeWithText("Seguidores", substring = true).assertExists()

        // Click en Seguir
        composeTestRule.onNodeWithText("Seguir", substring = true, ignoreCase = true).performClick()

        // Verificar aumento de seguidores (asume texto "1")
        composeTestRule.onNodeWithText("1").assertExists()

        // Volver al Home (botón atrás)
        composeTestRule.onNodeWithContentDescription("Volver", substring = true, ignoreCase = true).performClick()
        composeTestRule.onNodeWithContentDescription("Volver", substring = true, ignoreCase = true).performClick()

        // Ir a la sección "Siguiendo" del Feed
        composeTestRule.onNodeWithText("Siguiendo", substring = true, ignoreCase = true).performClick()

        // Verificar que aparezca el post del usuario seguido
        composeTestRule.onNodeWithText("Valle del Cocora", substring = true).assertExists()
    }
}
