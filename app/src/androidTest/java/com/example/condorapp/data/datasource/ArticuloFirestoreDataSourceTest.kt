package com.example.condorapp.data.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
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
class ArticuloFirestoreDataSourceTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var dataSource: ArticuloFirestoreDataSource

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        try {
            firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {}
        
        dataSource = ArticuloFirestoreDataSource(firestore)

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
    fun testGetArticuloById_ThrowsExceptionWhenNotFound() = runBlocking {
        var didThrow = false
        try {
            dataSource.getArticuloById("no_existe")
        } catch (e: Exception) {
            didThrow = true
        }
        assertTrue("Debería lanzar excepción si no existe", didThrow)
    }

    @Test
    fun testGetAllArticulos_ReturnsEmptyListInitially() = runBlocking {
        val articulos = dataSource.getAllArticulos()
        assertTrue(articulos.isEmpty())
    }

    @Test
    fun testInsertAndGetArticulo() = runBlocking {
        // Manual insert for testing
        val dto = ArticuloDto(titulo = "Test", tipo = "Lugar")
        val ref = firestore.collection("articulos").add(dto).await()
        
        val articulos = dataSource.getAllArticulos()
        assertEquals(1, articulos.size)
        assertEquals("Test", articulos[0].titulo)
        
        val single = dataSource.getArticuloById(ref.id)
        assertEquals("Lugar", single.tipo)
    }
}
