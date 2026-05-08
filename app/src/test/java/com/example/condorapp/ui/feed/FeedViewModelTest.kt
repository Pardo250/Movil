package com.example.condorapp.ui.feed

import com.example.condorapp.MainDispatcherRule
import com.example.condorapp.data.Articulo
import com.example.condorapp.data.repository.ArticuloRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
        assertThat(viewModel.uiState.value.articulos).hasSize(1)
        assertThat(viewModel.uiState.value.articulos.first().titulo).isEqualTo("Playa")
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `init sets errorMessage when loading fails`() = runTest {
        // Arrange
        coEvery { repository.getAllArticulos() } returns Result.failure(Exception("Error de red"))

        // Act
        val viewModel = FeedViewModel(repository)

        // Assert
        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Error de red")
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onCategorySelected updates state correctly`() = runTest {
        // Arrange
        coEvery { repository.getAllArticulos() } returns Result.success(emptyList())
        val viewModel = FeedViewModel(repository)

        // Act
        viewModel.onCategorySelected(2)

        // Assert
        assertThat(viewModel.uiState.value.selectedCategoryIndex).isEqualTo(2)
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
        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("Playa")
        assertThat(viewModel.uiState.value.filteredArticulos).hasSize(1)
        assertThat(viewModel.uiState.value.filteredArticulos.first().titulo).isEqualTo("Playa Blanca")
    }

    @Test
    fun `init with empty result shows empty list and no error`() = runTest {
        // Arrange
        coEvery { repository.getAllArticulos() } returns Result.success(emptyList())

        // Act
        val viewModel = FeedViewModel(repository)

        // Assert
        assertThat(viewModel.uiState.value.articulos).isEmpty()
        assertThat(viewModel.uiState.value.filteredArticulos).isEmpty()
        assertThat(viewModel.uiState.value.errorMessage).isNull()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}
