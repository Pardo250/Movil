package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.CreateReviewDto
import com.example.condorapp.data.dto.ReviewDto
import com.example.condorapp.data.dto.UpdateReviewDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * DataSource de reviews para Firestore.
 * Implementa la misma interfaz que ReviewRemoteDataSource (Retrofit),
 * permitiendo intercambiar backend con un solo cambio en la inyección.
 *
 * Colección Firestore: "reviews"
 * Usa whereEqualTo para filtrar por articuloId o usuarioId (no hay JOINs en NoSQL).
 * Los campos desnormalizados (usuarioNombre) se guardan directamente en el documento.
 */
class ReviewFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : ReviewDataSource {

    private val collection = db.collection("reviews")

    override suspend fun getReviewsByArticulo(articuloId: String): List<ReviewDto> {
        val snapshot = collection
            .whereEqualTo("articuloId", articuloId)
            .get()
            .await()
        return snapshot.documents.map { doc ->
            doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
                ?: ReviewDto(id = doc.id)
        }
    }

    override suspend fun getReviewsByUsuario(usuarioId: String): List<ReviewDto> {
        val snapshot = collection
            .whereEqualTo("usuarioId", usuarioId)
            .get()
            .await()
        return snapshot.documents.map { doc ->
            doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
                ?: ReviewDto(id = doc.id)
        }
    }

    override suspend fun createReview(dto: CreateReviewDto): ReviewDto {
        val docRef = collection.add(dto).await()
        // Leemos el documento recién creado para retornar el ReviewDto completo
        val doc = docRef.get().await()
        return doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
            ?: ReviewDto(
                id = doc.id,
                contenido = dto.contenido,
                calificacion = dto.calificacion,
                usuarioId = dto.usuarioId,
                articuloId = dto.articuloId,
                usuarioNombre = dto.usuarioNombre
            )
    }

    override suspend fun updateReview(id: String, dto: UpdateReviewDto): ReviewDto {
        val fields = mutableMapOf<String, Any>()
        dto.contenido?.let { fields["contenido"] = it }
        dto.calificacion?.let { fields["calificacion"] = it }

        collection.document(id).set(fields, SetOptions.merge()).await()

        // Retornar el documento actualizado
        val doc = collection.document(id).get().await()
        return doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
            ?: throw Exception("Error al actualizar review $id")
    }

    override suspend fun deleteReview(id: String) {
        collection.document(id).delete().await()
    }

    override suspend fun toggleLike(reviewId: String, userId: String): Boolean {
        return db.runTransaction { transaction ->
            val reviewRef = collection.document(reviewId)
            val likeRef = reviewRef.collection("likes").document(userId)
            
            val likeDoc = transaction.get(likeRef)
            val isCurrentlyLiked = likeDoc.exists()

            if (isCurrentlyLiked) {
                // Unlike
                transaction.delete(likeRef)
                transaction.update(reviewRef, "likesCount", FieldValue.increment(-1))
                false
            } else {
                // Like
                transaction.set(likeRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(reviewRef, "likesCount", FieldValue.increment(1))
                true
            }
        }.await()
    }

    override suspend fun isLikedByUser(reviewId: String, userId: String): Boolean {
        val doc = collection.document(reviewId).collection("likes").document(userId).get().await()
        return doc.exists()
    }

    override fun listenReviewsByArticulo(articuloId: String): Flow<List<ReviewDto>> = callbackFlow {
        val listener = collection.whereEqualTo("articuloId", articuloId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null) {
                    val reviews = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
                    }
                    trySend(reviews)
                }
            }
            
        awaitClose { listener.remove() }
    }

    override suspend fun getAllReviews(): List<ReviewDto> {
        val snapshot = collection.get().await()
        return snapshot.documents.map { doc ->
            doc.toObject(ReviewDto::class.java)?.copy(id = doc.id)
                ?: ReviewDto(id = doc.id)
        }
    }
}
