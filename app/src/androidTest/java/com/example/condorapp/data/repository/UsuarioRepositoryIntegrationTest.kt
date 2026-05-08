package com.example.condorapp.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.datasource.UsuarioFirestoreDataSource
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
class UsuarioRepositoryIntegrationTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var dataSource: UsuarioFirestoreDataSource
    private lateinit var repository: UsuarioRepository

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        try { firestore.useEmulator("10.0.2.2", 8080) } catch (e: Exception) {}
        
        dataSource = UsuarioFirestoreDataSource(firestore)
        repository = UsuarioRepository(dataSource)

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

    @Test
    fun testSaveAndGetUsuario_Integration() = runBlocking {
        // Act
        val saveResult = repository.saveUsuario("u100", "Ana", "ana@test.com", "ana123")

        // Assert
        assertThat(saveResult.isSuccess).isTrue()

        val getResult = repository.getUsuarioById("u100")
        assertThat(getResult.isSuccess).isTrue()
        assertThat(getResult.getOrNull()?.nombre).isEqualTo("Ana")
    }

    @Test
    fun testUpdateUsuario_Integration() = runBlocking {
        // Arrange
        repository.saveUsuario("u200", "Beto", "beto@t.com", "beto")

        // Act
        val updateResult = repository.updateUsuario("u200", mapOf("bio" to "Nueva Bio de Beto"))
        assertThat(updateResult.isSuccess).isTrue()

        // Assert
        val getResult = repository.getUsuarioById("u200")
        assertThat(getResult.getOrNull()?.bio).isEqualTo("Nueva Bio de Beto")
    }

    @Test
    fun testToggleFollow_Integration() = runBlocking {
        // Arrange
        repository.saveUsuario("f1", "Follower", "f1@t.com", "f1")
        repository.saveUsuario("f2", "Following", "f2@t.com", "f2")

        // Act: Follow
        val result1 = repository.toggleFollow("f1", "f2")
        assertThat(result1.getOrNull()).isTrue()

        // Assert
        val followingList = repository.getFollowing("f1")
        assertThat(followingList.getOrNull()).hasSize(1)
    }

    @Test
    fun testGetAllUsuarios_Integration() = runBlocking {
        // Arrange
        repository.saveUsuario("u1", "Alice", "a@t.com", "alice")
        repository.saveUsuario("u2", "Bob", "b@t.com", "bob")

        // Act
        val result = repository.getAllUsuarios()

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(2)
        assertThat(result.getOrNull()?.map { it.nombre }).containsExactly("Alice", "Bob")
    }
}
