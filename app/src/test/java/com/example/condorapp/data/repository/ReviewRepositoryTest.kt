package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.ReviewDataSource
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.CreateReviewDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ReviewRepositoryTest {

    private val dataSource: ReviewDataSource = mockk()
    private val repository = ReviewRepository(dataSource)

    // ─── Tests existentes migrados a Truth ──────────────────────

    @Test
    fun `getReviewsByArticulo maps DTO to Review successfully`() = runTest {
        // Arrange
        val dtos = listOf(ReviewDto(id = "1", contenido = "Genial", calificacion = 5))
        coEvery { dataSource.getReviewsByArticulo("art1") } returns dtos

        // Act
        val result = repository.getReviewsByArticulo("art1")

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.first()?.comment).isEqualTo("Genial")
        assertThat(result.getOrNull()?.first()?.rating).isEqualTo(5)
    }

    @Test
    fun `getReviewsByArticulo returns failure when dataSource throws`() = runTest {
        // Arrange
        coEvery { dataSource.getReviewsByArticulo("art2") } throws Exception("DB error")

        // Act
        val result = repository.getReviewsByArticulo("art2")

        // Assert
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `createReview returns a correctly mapped Review`() = runTest {
        // Arrange
        val dto = ReviewDto(id = "rev1", contenido = "Buen post", calificacion = 4, usuarioId = "u1", usuarioNombre = "Pedro")
        coEvery { dataSource.createReview(any()) } returns dto

        // Act
        val result = repository.createReview("Buen post", 4, "u1", "art1", "Pedro")

        // Assert
        assertThat(result.isSuccess).isTrue()
        val review = result.getOrNull()
        assertThat(review?.id).isEqualTo("rev1")
        assertThat(review?.name).isEqualTo("Pedro")
        assertThat(review?.rating).isEqualTo(4)
    }

    @Test
    fun `deleteReview calls dataSource and returns success`() = runTest {
        // Arrange
        coEvery { dataSource.deleteReview("r1") } returns Unit

        // Act
        val result = repository.deleteReview("r1")

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify(exactly = 1) { dataSource.deleteReview("r1") }
    }

    // ─── Tests de mapeo DTO → Modelo (Sprint 13 req.) ───────────

    @Test
    fun `getReviewsByArticulo maps null usuario to Usuario desconocido`() = runTest {
        // Arrange — DTO donde usuario es null y usuarioNombre está vacío
        val dtos = listOf(
            ReviewDto(id = "r1", contenido = "Sin autor", calificacion = 3, usuario = null, usuarioNombre = "")
        )
        coEvery { dataSource.getReviewsByArticulo("art5") } returns dtos

        // Act
        val result = repository.getReviewsByArticulo("art5")

        // Assert — El mapper debe poner "Usuario desconocido" cuando no hay nombre
        assertThat(result.getOrNull()?.first()?.name).isEqualTo("Usuario desconocido")
    }

    @Test
    fun `createReview maps empty usuarioNombre to Tu`() = runTest {
        // Arrange — DTO devuelto con usuarioNombre vacío
        val dto = ReviewDto(id = "r2", contenido = "Mi review", calificacion = 5, usuarioId = "u2", usuarioNombre = "")
        coEvery { dataSource.createReview(any()) } returns dto

        // Act
        val result = repository.createReview("Mi review", 5, "u2", "art6", "")

        // Assert — createReview usa dto.usuarioNombre.ifEmpty { "Tú" }
        assertThat(result.getOrNull()?.name).isEqualTo("Tú")
    }

    @Test
    fun `toggleLike returns success with boolean`() = runTest {
        // Arrange
        coEvery { dataSource.toggleLike("r1", "u1") } returns true

        // Act
        val result = repository.toggleLike("r1", "u1")

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()
    }

    @Test
    fun `getAllReviews returns mapped list on success`() = runTest {
        // Arrange
        val dtos = listOf(
            ReviewDto(id = "r1", contenido = "A", calificacion = 5, usuarioNombre = "Ana"),
            ReviewDto(id = "r2", contenido = "B", calificacion = 3, usuarioNombre = "Bob")
        )
        coEvery { dataSource.getAllReviews() } returns dtos

        // Act
        val result = repository.getAllReviews()

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(2)
        assertThat(result.getOrNull()?.map { it.name }).containsExactly("Ana", "Bob")
    }
}
