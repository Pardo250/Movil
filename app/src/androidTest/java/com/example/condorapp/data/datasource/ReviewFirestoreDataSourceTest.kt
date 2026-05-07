package com.example.condorapp.data.datasource

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
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
        val dto = CreateReviewDto("Excelente lugar", 5, "u1", "a1", "User1")
        val created = dataSource.createReview(dto)

        val reviewsByArticulo = dataSource.getReviewsByArticulo("a1")
        assertEquals(1, reviewsByArticulo.size)
        assertEquals("Excelente lugar", reviewsByArticulo[0].contenido)
        assertEquals(created.id, reviewsByArticulo[0].id)
    }

    @Test
    fun testUpdateReview() = runBlocking {
        val dto = CreateReviewDto("Malo", 1, "u2", "a2", "User2")
        val created = dataSource.createReview(dto)

        val updated = dataSource.updateReview(created.id, UpdateReviewDto("Bueno", 4))
        assertEquals("Bueno", updated.contenido)
        assertEquals(4, updated.calificacion)
    }

    @Test
    fun testDeleteReview() = runBlocking {
        val dto = CreateReviewDto("Test", 3, "u3", "a3", "User3")
        val created = dataSource.createReview(dto)
        
        dataSource.deleteReview(created.id)

        val reviews = dataSource.getReviewsByArticulo("a3")
        assertTrue(reviews.isEmpty())
    }
}
