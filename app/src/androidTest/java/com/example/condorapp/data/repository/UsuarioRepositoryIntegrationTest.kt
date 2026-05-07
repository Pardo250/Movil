package com.example.condorapp.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.datasource.UsuarioFirestoreDataSource
import com.example.condorapp.data.dto.UsuarioDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        // Act 1: Save
        val saveResult = repository.saveUsuario("u100", "Ana", "ana@test.com", "ana123")
        assertTrue(saveResult.isSuccess)

        // Act 2: Get
        val getResult = repository.getUsuarioById("u100")
        assertTrue(getResult.isSuccess)
        assertEquals("Ana", getResult.getOrNull()?.nombre)
    }

    @Test
    fun testUpdateUsuario_Integration() = runBlocking {
        // Arrange
        repository.saveUsuario("u200", "Beto", "beto@t.com", "beto")

        // Act
        val updateResult = repository.updateUsuario("u200", mapOf("bio" to "Nueva Bio de Beto"))
        assertTrue(updateResult.isSuccess)

        // Assert
        val getResult = repository.getUsuarioById("u200")
        assertEquals("Nueva Bio de Beto", getResult.getOrNull()?.bio)
    }

    @Test
    fun testToggleFollow_Integration() = runBlocking {
        // Arrange
        repository.saveUsuario("f1", "Follower", "f1@t.com", "f1")
        repository.saveUsuario("f2", "Following", "f2@t.com", "f2")

        // Act: Follow
        val result1 = repository.toggleFollow("f1", "f2")
        assertTrue(result1.getOrNull() == true) // Ahora sigue

        // Assert
        val followingList = repository.getFollowing("f1")
        assertEquals(1, followingList.getOrNull()?.size)
    }
}
