package com.example.condorapp.data.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.dto.ArticuloDto
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
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
        // Arrange
        var thrownException: Exception? = null

        // Act
        try {
            dataSource.getArticuloById("no_existe")
        } catch (e: Exception) {
            thrownException = e
        }

        // Assert
        assertThat(thrownException).isNotNull()
        assertThat(thrownException?.message).contains("no encontrado")
    }

    @Test
    fun testGetAllArticulos_ReturnsEmptyListInitially() = runBlocking {
        // Arrange (nothing)

        // Act
        val articulos = dataSource.getAllArticulos()

        // Assert
        assertThat(articulos).isEmpty()
    }

    @Test
    fun testInsertAndGetArticulo() = runBlocking {
        // Arrange
        val dto = ArticuloDto(titulo = "Test", tipo = "Lugar", descripcion = "Desc test")
        val ref = firestore.collection("articulos").add(dto).await()

        // Act
        val articulos = dataSource.getAllArticulos()
        val single = dataSource.getArticuloById(ref.id)

        // Assert
        assertThat(articulos).hasSize(1)
        assertThat(articulos[0].titulo).isEqualTo("Test")
        assertThat(single.tipo).isEqualTo("Lugar")
        assertThat(single.descripcion).isEqualTo("Desc test")
    }
}
