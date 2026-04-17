package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.ArticuloDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * DataSource de artículos para Firestore.
 * Implementa la misma interfaz que ArticuloRemoteDataSource (Retrofit),
 * permitiendo intercambiar backend con un solo cambio en la inyección.
 *
 * Colección Firestore: "articulos"
 */
class ArticuloFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : ArticuloDataSource {

    private val collection = db.collection("articulos")

    override suspend fun getAllArticulos(): List<ArticuloDto> {
        val snapshot = collection.get().await()
        return snapshot.documents.map { doc ->
            doc.toObject(ArticuloDto::class.java)?.copy(id = doc.id)
                ?: ArticuloDto(id = doc.id)
        }
    }

    override suspend fun getArticuloById(id: String): ArticuloDto {
        val doc = collection.document(id).get().await()
        return doc.toObject(ArticuloDto::class.java)?.copy(id = doc.id)
            ?: throw Exception("Artículo no encontrado (id=$id)")
    }
}
