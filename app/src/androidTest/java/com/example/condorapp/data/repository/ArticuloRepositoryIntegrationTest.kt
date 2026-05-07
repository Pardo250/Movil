package com.example.condorapp.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.datasource.ArticuloFirestoreDataSource
import com.example.condorapp.data.dto.ArticuloDto
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
class ArticuloRepositoryIntegrationTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var dataSource: ArticuloFirestoreDataSource
    private lateinit var repository: ArticuloRepository

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        try { firestore.useEmulator("10.0.2.2", 8080) } catch (e: Exception) {}
        
        dataSource = ArticuloFirestoreDataSource(firestore)
        repository = ArticuloRepository(dataSource)

        runBlocking {
            val docs = firestore.collection("articulos").get().await()
            for (doc in docs) { doc.reference.delete().await() }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            val docs = firestore.collection("articulos").get().await()
            for (doc in docs) { doc.reference.delete().await() }
        }
    }

    @Test
    fun testGetAllArticulos_Integration() = runBlocking {
        // Arrange
        firestore.collection("articulos").add(ArticuloDto(titulo = "Art 1", tipo = "Museo")).await()

        // Act
        val result = repository.getAllArticulos()

        // Assert
        assertTrue(result.isSuccess)
        val list = result.getOrNull()
        assertEquals(1, list?.size)
        assertEquals("Art 1", list?.first()?.titulo)
    }

    @Test
    fun testGetArticuloById_Integration() = runBlocking {
        // Arrange
        val ref = firestore.collection("articulos").add(ArticuloDto(titulo = "Art 2", tipo = "Parque")).await()

        // Act
        val result = repository.getArticuloById(ref.id)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Art 2", result.getOrNull()?.titulo)
    }
}
