package com.example.condorapp.data.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.dto.UsuarioDto
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UsuarioFirestoreDataSourceTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var dataSource: UsuarioFirestoreDataSource

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        try {
            firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {}
        
        dataSource = UsuarioFirestoreDataSource(firestore)

        runBlocking {
            val docs = firestore.collection("usuarios").get().await()
            for (doc in docs) { doc.reference.delete().await() }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            val docs = firestore.collection("usuarios").get().await()
            for (doc in docs) { doc.reference.delete().await() }
        }
    }

    // ─── Arrange / Act / Assert ─────────────────────────────────

    @Test
    fun testSaveAndGetUsuario() = runBlocking {
        // Arrange
        val dto = UsuarioDto(id = "user123", nombre = "Carlos", email = "carlos@test.com")

        // Act
        dataSource.saveUsuario("user123", dto)
        val retrieved = dataSource.getUsuarioById("user123")

        // Assert
        assertThat(retrieved).isNotNull()
        assertThat(retrieved.nombre).isEqualTo("Carlos")
        assertThat(retrieved.email).isEqualTo("carlos@test.com")
    }

    @Test
    fun testUpdateUsuario() = runBlocking {
        // Arrange
        val dto = UsuarioDto(id = "user123", nombre = "Carlos", bio = "Bio 1")
        dataSource.saveUsuario("user123", dto)

        // Act
        dataSource.updateUsuario("user123", mapOf("bio" to "Nueva bio"))
        val retrieved = dataSource.getUsuarioById("user123")

        // Assert
        assertThat(retrieved.nombre).isEqualTo("Carlos") // Permanece igual
        assertThat(retrieved.bio).isEqualTo("Nueva bio")  // Actualizado
    }

    @Test
    fun testToggleFollow() = runBlocking {
        val follower = java.util.UUID.randomUUID().toString()
        val following = java.util.UUID.randomUUID().toString()

        // Arrange
        dataSource.saveUsuario(follower, UsuarioDto(id = follower, nombre = "A"))
        dataSource.saveUsuario(following, UsuarioDto(id = following, nombre = "B"))

        // Act & Assert 1: Seguir
        val nowFollowing = dataSource.toggleFollow(follower, following)
        assertThat(nowFollowing).isTrue()
        assertThat(dataSource.isFollowing(follower, following)).isTrue()

        // Act & Assert 2: Dejar de seguir
        val nowFollowing2 = dataSource.toggleFollow(follower, following)
        assertThat(nowFollowing2).isFalse()
        assertThat(dataSource.isFollowing(follower, following)).isFalse()
    }

    @Test
    fun testGetAllUsuarios_ReturnsSavedUsers() = runBlocking {
        // Arrange
        dataSource.saveUsuario("u1", UsuarioDto(id = "u1", nombre = "Alice"))
        dataSource.saveUsuario("u2", UsuarioDto(id = "u2", nombre = "Bob"))

        // Act
        val result = dataSource.getAllUsuarios()

        // Assert
        assertThat(result).hasSize(2)
        assertThat(result.map { it.nombre }).containsExactly("Alice", "Bob")
        Unit
    }

    @Test
    fun testGetUsuarioById_ThrowsWhenNotFound() = runBlocking {
        // Arrange (no users saved)
        var thrownException: Exception? = null

        // Act
        try {
            dataSource.getUsuarioById("no_existe")
        } catch (e: Exception) {
            thrownException = e
        }

        // Assert
        assertThat(thrownException).isNotNull()
        assertThat(thrownException?.message).contains("no encontrado")
    }
}
