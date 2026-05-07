package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.ReviewDataSource
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.CreateReviewDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewRepositoryTest {

    private val dataSource: ReviewDataSource = mockk()
    private val repository = ReviewRepository(dataSource)

    @Test
    fun `getReviewsByArticulo maps DTO to Review successfully`() = runTest {
        // Arrange
        val dtos = listOf(ReviewDto(id = "1", contenido = "Genial", calificacion = 5))
        coEvery { dataSource.getReviewsByArticulo("art1") } returns dtos

        // Act
        val result = repository.getReviewsByArticulo("art1")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Genial", result.getOrNull()?.first()?.comment)
        assertEquals(5, result.getOrNull()?.first()?.rating)
    }

    @Test
    fun `getReviewsByArticulo returns failure when dataSource throws`() = runTest {
        // Arrange
        coEvery { dataSource.getReviewsByArticulo("art2") } throws Exception("DB error")

        // Act
        val result = repository.getReviewsByArticulo("art2")

        // Assert
        assertTrue(result.isFailure)
    }

    @Test
    fun `createReview returns a correctly mapped Review`() = runTest {
        // Arrange
        val dto = ReviewDto(id = "rev1", contenido = "Buen post", calificacion = 4, usuarioId = "u1", usuarioNombre = "Pedro")
        coEvery { dataSource.createReview(any()) } returns dto

        // Act
        val result = repository.createReview("Buen post", 4, "u1", "art1", "Pedro")

        // Assert
        assertTrue(result.isSuccess)
        val review = result.getOrNull()
        assertEquals("rev1", review?.id)
        assertEquals("Pedro", review?.name)
        assertEquals(4, review?.rating)
    }

    @Test
    fun `deleteReview calls dataSource and returns success`() = runTest {
        // Arrange
        coEvery { dataSource.deleteReview("r1") } returns Unit

        // Act
        val result = repository.deleteReview("r1")

        // Assert
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { dataSource.deleteReview("r1") }
    }
}
