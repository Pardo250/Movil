package com.example.condorapp.ui.feed

import com.example.condorapp.MainDispatcherRule
import com.example.condorapp.data.Articulo
import com.example.condorapp.data.repository.ArticuloRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: ArticuloRepository = mockk()

    @Test
    fun `init loads articulos successfully`() = runTest {
        // Arrange
        val articulos = listOf(Articulo("1", "Playa", "Linda", "playa"))
        coEvery { repository.getAllArticulos() } returns Result.success(articulos)

        // Act
        val viewModel = FeedViewModel(repository)

        // Assert
        assertEquals(1, viewModel.uiState.value.articulos.size)
        assertEquals("Playa", viewModel.uiState.value.articulos.first().titulo)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `init sets errorMessage when loading fails`() = runTest {
        // Arrange
        coEvery { repository.getAllArticulos() } returns Result.failure(Exception("Error de red"))

        // Act
        val viewModel = FeedViewModel(repository)

        // Assert
        assertEquals("Error de red", viewModel.uiState.value.errorMessage)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onCategorySelected updates state correctly`() = runTest {
        // Arrange
        coEvery { repository.getAllArticulos() } returns Result.success(emptyList())
        val viewModel = FeedViewModel(repository)

        // Act
        viewModel.onCategorySelected(2)

        // Assert
        assertEquals(2, viewModel.uiState.value.selectedCategoryIndex)
    }

    @Test
    fun `onSearchQueryChange filters articulos`() = runTest {
        // Arrange
        val articulos = listOf(
            Articulo("1", "Valle Cocora", "Verde", "montaña"),
            Articulo("2", "Playa Blanca", "Azul", "playa")
        )
        coEvery { repository.getAllArticulos() } returns Result.success(articulos)
        val viewModel = FeedViewModel(repository)

        // Act
        viewModel.onSearchQueryChange("Playa")

        // Assert
        assertEquals("Playa", viewModel.uiState.value.searchQuery)
        assertEquals(1, viewModel.uiState.value.filteredArticulos.size)
        assertEquals("Playa Blanca", viewModel.uiState.value.filteredArticulos.first().titulo)
    }
}
