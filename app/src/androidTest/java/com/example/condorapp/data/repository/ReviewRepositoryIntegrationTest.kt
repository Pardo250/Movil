package com.example.condorapp.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.datasource.ReviewFirestoreDataSource
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
class ReviewRepositoryIntegrationTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var dataSource: ReviewFirestoreDataSource
    private lateinit var repository: ReviewRepository

    @Before
    fun setup() {
        firestore = FirebaseFirestore.getInstance()
        try { firestore.useEmulator("10.0.2.2", 8080) } catch (e: Exception) {}
        
        dataSource = ReviewFirestoreDataSource(firestore)
        repository = ReviewRepository(dataSource)

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
    fun testCreateAndGetReview_Integration() = runBlocking {
        // Act
        val resultCreate = repository.createReview("Me encantó", 5, "u1", "art1", "Juan")
        assertTrue(resultCreate.isSuccess)

        // Assert
        val resultGet = repository.getReviewsByArticulo("art1")
        val reviews = resultGet.getOrNull()
        assertEquals(1, reviews?.size)
        assertEquals("Me encantó", reviews?.first()?.comment)
        assertEquals("Juan", reviews?.first()?.name)
    }

    @Test
    fun testUpdateReview_Integration() = runBlocking {
        // Arrange
        val created = repository.createReview("Ok", 3, "u2", "art2", "Pepe").getOrNull()!!

        // Act
        val resultUpdate = repository.updateReview(created.id, "Muy bueno", 4)
        assertTrue(resultUpdate.isSuccess)

        // Assert
        val review = repository.getReviewsByArticulo("art2").getOrNull()?.first()
        assertEquals("Muy bueno", review?.comment)
        assertEquals(4, review?.rating)
    }

    @Test
    fun testDeleteReview_Integration() = runBlocking {
        // Arrange
        val created = repository.createReview("A borrar", 1, "u3", "art3", "Dani").getOrNull()!!

        // Act
        val resultDelete = repository.deleteReview(created.id)
        assertTrue(resultDelete.isSuccess)

        // Assert
        val reviews = repository.getReviewsByArticulo("art3").getOrNull()
        assertTrue(reviews?.isEmpty() == true)
    }
}
