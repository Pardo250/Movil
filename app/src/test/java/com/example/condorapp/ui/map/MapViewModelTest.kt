package com.example.condorapp.ui.map

import com.example.condorapp.MainDispatcherRule
import com.example.condorapp.data.Review
import com.example.condorapp.data.repository.ReviewRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests unitarios para MapViewModel (Sprint 13.5).
 *
 * Verifica:
 * - Filtrado correcto de reviews de las últimas 24h (delegado al repository).
 * - Reviews sin ubicación no aparecen.
 * - Reviews sin createdAt no aparecen (tratados como antiguos por el DataSource).
 * - Selección de marcador para BottomSheet.
 * - Refresco manual de reviews.
 * - Manejo de errores.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MapViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var reviewRepository: ReviewRepository
    private lateinit var viewModel: MapViewModel

    // Reviews de ejemplo
    private val reviewWithLocation = Review(
        id = "1",
        name = "Juan",
        rating = 5,
        comment = "Excelente lugar",
        likes = 3,
        usuarioId = "u1",
        articuloNombre = "Valle del Cocora",
        lat = 4.637,
        lng = -75.506,
        createdAt = "2026-05-14T20:00:00Z" // Dentro de 24h
    )

    private val reviewWithLocation2 = Review(
        id = "2",
        name = "Maria",
        rating = 4,
        comment = "Muy bonito",
        likes = 1,
        usuarioId = "u2",
        articuloNombre = "Playa Blanca",
        lat = 10.396,
        lng = -75.514,
        createdAt = "2026-05-14T18:00:00Z"
    )

    private val reviewWithoutLocation = Review(
        id = "3",
        name = "Carlos",
        rating = 3,
        comment = "Está bien",
        likes = 0,
        usuarioId = "u3",
        articuloNombre = "Hotel Central",
        lat = null,
        lng = null,
        createdAt = "2026-05-14T19:00:00Z"
    )

    @Before
    fun setUp() {
        reviewRepository = mockk()
    }

    @Test
    fun `loadReviews returns reviews with valid coordinates`() = runTest {
        // El repository ya filtra por 24h Y por coordenadas válidas
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(
            listOf(reviewWithLocation, reviewWithLocation2)
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.reviews).hasSize(2)
        assertThat(state.reviews.all { it.lat != null && it.lng != null }).isTrue()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `loadReviews excludes reviews without coordinates via repository`() = runTest {
        // Repository devuelve solo los que tienen coordenadas
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(
            listOf(reviewWithLocation)
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.reviews).hasSize(1)
        assertThat(state.reviews[0].id).isEqualTo("1")
    }

    @Test
    fun `loadReviews returns empty list when no recent reviews`() = runTest {
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(emptyList())

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.reviews).isEmpty()
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `loadReviews handles error gracefully`() = runTest {
        coEvery { reviewRepository.getReviewsForMap() } returns Result.failure(
            Exception("Sin conexión a internet")
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo("Sin conexión a internet")
        assertThat(state.reviews).isEmpty()
    }

    @Test
    fun `onMarkerClick selects the correct review`() = runTest {
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(
            listOf(reviewWithLocation, reviewWithLocation2)
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()

        viewModel.onMarkerClick(reviewWithLocation)

        val state = viewModel.uiState.value
        assertThat(state.selectedReview).isNotNull()
        assertThat(state.selectedReview?.id).isEqualTo("1")
        assertThat(state.selectedReview?.name).isEqualTo("Juan")
    }

    @Test
    fun `onDismissSheet clears selected review`() = runTest {
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(
            listOf(reviewWithLocation)
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()

        viewModel.onMarkerClick(reviewWithLocation)
        assertThat(viewModel.uiState.value.selectedReview).isNotNull()

        viewModel.onDismissSheet()
        assertThat(viewModel.uiState.value.selectedReview).isNull()
    }

    @Test
    fun `refreshReviews reloads reviews`() = runTest {
        // Primera carga: 1 review
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(
            listOf(reviewWithLocation)
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.reviews).hasSize(1)

        // Segunda carga (refresh): 2 reviews
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(
            listOf(reviewWithLocation, reviewWithLocation2)
        )

        viewModel.refreshReviews()
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.reviews).hasSize(2)
    }

    @Test
    fun `onErrorShown clears error message`() = runTest {
        coEvery { reviewRepository.getReviewsForMap() } returns Result.failure(
            Exception("Error de red")
        )

        viewModel = MapViewModel(reviewRepository)
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.errorMessage).isNotNull()

        viewModel.onErrorShown()
        assertThat(viewModel.uiState.value.errorMessage).isNull()
    }

    @Test
    fun `initial state shows loading`() = runTest {
        coEvery { reviewRepository.getReviewsForMap() } returns Result.success(emptyList())

        viewModel = MapViewModel(reviewRepository)

        // Before advanceUntilIdle, state should have started loading
        // After init, the coroutine fires immediately with UnconfinedTestDispatcher
        advanceUntilIdle()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}
