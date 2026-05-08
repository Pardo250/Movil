package com.example.condorapp.data.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.google.common.truth.Truth.assertThat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewFirestoreDataSourceTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var dataSource: ReviewFirestoreDataSource

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        try {
            firestore.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {}
        
        dataSource = ReviewFirestoreDataSource(firestore)

        runBlocking {
            val docs = firestore.collection("reviews").get().await()
            for (doc in docs) { doc.reference.delete().await() }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            val docs = firestore.collection("reviews").get().await()
            for (doc in docs) { doc.reference.delete().await() }
        }
    }

    @Test
    fun testCreateAndGetReviews() = runBlocking {
        // Arrange
        val dto = CreateReviewDto("Excelente lugar", 5, "u1", "a1", "User1")

        // Act
        val created = dataSource.createReview(dto)
        val reviewsByArticulo = dataSource.getReviewsByArticulo("a1")

        // Assert
        assertThat(reviewsByArticulo).hasSize(1)
        assertThat(reviewsByArticulo[0].contenido).isEqualTo("Excelente lugar")
        assertThat(reviewsByArticulo[0].id).isEqualTo(created.id)
    }

    @Test
    fun testUpdateReview() = runBlocking {
        // Arrange
        val dto = CreateReviewDto("Malo", 1, "u2", "a2", "User2")
        val created = dataSource.createReview(dto)

        // Act
        val updated = dataSource.updateReview(created.id, UpdateReviewDto("Bueno", 4))

        // Assert
        assertThat(updated.contenido).isEqualTo("Bueno")
        assertThat(updated.calificacion).isEqualTo(4)
    }

    @Test
    fun testDeleteReview() = runBlocking {
        // Arrange
        val dto = CreateReviewDto("Test", 3, "u3", "a3", "User3")
        val created = dataSource.createReview(dto)

        // Act
        dataSource.deleteReview(created.id)
        val reviews = dataSource.getReviewsByArticulo("a3")

        // Assert
        assertThat(reviews).isEmpty()
    }

    @Test
    fun testToggleLike_LikeAndUnlike() = runBlocking {
        // Arrange
        val dto = CreateReviewDto("Like test", 4, "u4", "a4", "User4")
        val created = dataSource.createReview(dto)

        // Act 1: Like
        val isLiked = dataSource.toggleLike(created.id, "voter1")

        // Assert 1
        assertThat(isLiked).isTrue()
        assertThat(dataSource.isLikedByUser(created.id, "voter1")).isTrue()

        // Act 2: Unlike
        val isUnliked = dataSource.toggleLike(created.id, "voter1")

        // Assert 2
        assertThat(isUnliked).isFalse()
        assertThat(dataSource.isLikedByUser(created.id, "voter1")).isFalse()
    }

    @Test
    fun testGetReviewsByUsuario() = runBlocking {
        // Arrange
        dataSource.createReview(CreateReviewDto("R1", 5, "userX", "a1", "UserX"))
        dataSource.createReview(CreateReviewDto("R2", 3, "userX", "a2", "UserX"))
        dataSource.createReview(CreateReviewDto("R3", 2, "userY", "a1", "UserY"))

        // Act
        val reviewsX = dataSource.getReviewsByUsuario("userX")
        val reviewsY = dataSource.getReviewsByUsuario("userY")

        // Assert
        assertThat(reviewsX).hasSize(2)
        assertThat(reviewsY).hasSize(1)
        assertThat(reviewsX.map { it.contenido }).containsExactly("R1", "R2")
        Unit
    }
}
