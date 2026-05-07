package com.example.condorapp.data.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.dto.UsuarioDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.*
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

    @Test
    fun testSaveAndGetUsuario() = runBlocking {
        val dto = UsuarioDto(id = "user123", nombre = "Carlos")
        dataSource.saveUsuario("user123", dto)

        val retrieved = dataSource.getUsuarioById("user123")
        assertEquals("Carlos", retrieved.nombre)
    }

    @Test
    fun testUpdateUsuario() = runBlocking {
        val dto = UsuarioDto(id = "user123", nombre = "Carlos", bio = "Bio 1")
        dataSource.saveUsuario("user123", dto)

        dataSource.updateUsuario("user123", mapOf("bio" to "Nueva bio"))

        val retrieved = dataSource.getUsuarioById("user123")
        assertEquals("Carlos", retrieved.nombre) // Permanece igual
        assertEquals("Nueva bio", retrieved.bio)  // Actualizado
    }

    @Test
    fun testToggleFollow() = runBlocking {
        dataSource.saveUsuario("follower", UsuarioDto(id = "follower", nombre = "A"))
        dataSource.saveUsuario("following", UsuarioDto(id = "following", nombre = "B"))

        // Act & Assert 1: Seguir
        val nowFollowing = dataSource.toggleFollow("follower", "following")
        assertTrue(nowFollowing)
        assertTrue(dataSource.isFollowing("follower", "following"))

        // Act & Assert 2: Dejar de seguir
        val nowFollowing2 = dataSource.toggleFollow("follower", "following")
        assertFalse(nowFollowing2)
        assertFalse(dataSource.isFollowing("follower", "following"))
    }
}
