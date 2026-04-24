package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * DataSource de usuarios para Firestore.
 * Implementa la misma interfaz que UsuarioRemoteDataSource (Retrofit),
 * permitiendo intercambiar backend con un solo cambio en la inyección.
 *
 * Colección Firestore: "usuarios"
 * El ID del documento es el UID de FirebaseAuth.
 */
class UsuarioFirestoreDataSource @Inject constructor(
    private val db: FirebaseFirestore
) : UsuarioDataSource {

    private val collection = db.collection("usuarios")

    override suspend fun getAllUsuarios(): List<UsuarioDto> {
        val snapshot = collection.get().await()
        return snapshot.documents.map { doc ->
            doc.toObject(UsuarioDto::class.java)?.copy(id = doc.id)
                ?: UsuarioDto(id = doc.id)
        }
    }

    override suspend fun getUsuarioById(id: String): UsuarioDto {
        val doc = collection.document(id).get().await()
        return doc.toObject(UsuarioDto::class.java)?.copy(id = doc.id)
            ?: throw Exception("Usuario no encontrado (id=$id)")
    }

    /**
     * Guarda un nuevo usuario en Firestore.
     * Se usa el UID de FirebaseAuth como ID del documento.
     */
    override suspend fun saveUsuario(id: String, dto: UsuarioDto) {
        collection.document(id).set(dto).await()
    }

    /**
     * Actualiza campos específicos del usuario usando merge.
     * Solo modifica los campos enviados en el mapa, sin sobreescribir el resto.
     */
    override suspend fun updateUsuario(id: String, fields: Map<String, Any>) {
        collection.document(id).set(fields, SetOptions.merge()).await()
    }

    override suspend fun toggleFollow(followerId: String, followingId: String): Boolean {
        // Obtener nombre del seguidor para la notificación
        val followerDoc = collection.document(followerId).get().await()
        val followerName = followerDoc.getString("nombre") ?: "Alguien"

        val nowFollowing = db.runTransaction { transaction ->
            val followerRef = collection.document(followerId)
            val followingRef = collection.document(followingId)
            
            val followingListRef = followerRef.collection("following").document(followingId)
            val followerListRef = followingRef.collection("followers").document(followerId)

            val followingDoc = transaction.get(followingListRef)
            val isCurrentlyFollowing = followingDoc.exists()

            if (isCurrentlyFollowing) {
                // Unfollow
                transaction.delete(followingListRef)
                transaction.delete(followerListRef)
                transaction.update(followerRef, "followingCount", FieldValue.increment(-1))
                transaction.update(followingRef, "followersCount", FieldValue.increment(-1))
                false
            } else {
                // Follow
                transaction.set(followingListRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followerListRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(followerRef, "followingCount", FieldValue.increment(1))
                transaction.update(followingRef, "followersCount", FieldValue.increment(1))
                true
            }
        }.await()

        // Guardar notificación en Firestore si acaba de seguir (no al dejar de seguir)
        if (nowFollowing) {
            try {
                val notifRef = db.collection("notifications")
                    .document(followingId)
                    .collection("items")
                    .document()
                notifRef.set(mapOf(
                    "type"      to "follow",
                    "userName"  to followerName,
                    "action"    to "ahora te sigue",
                    "time"      to "Ahora",
                    "avatarUrl" to "",
                    "createdAt" to System.currentTimeMillis()
                )).await()
            } catch (_: Exception) {}
        }

        return nowFollowing
    }

    override suspend fun isFollowing(followerId: String, followingId: String): Boolean {
        val doc = collection.document(followerId).collection("following").document(followingId).get().await()
        return doc.exists()
    }

    override suspend fun getFollowers(userId: String): List<UsuarioDto> {
        val followersSnapshot = collection.document(userId).collection("followers").get().await()
        val followerIds = followersSnapshot.documents.map { it.id }
        
        if (followerIds.isEmpty()) return emptyList()
        
        // Firestore 'in' query allows max 10 elements. We might need to chunk it, but for now we do simple query.
        val chunks = followerIds.chunked(10)
        val result = mutableListOf<UsuarioDto>()
        for (chunk in chunks) {
            val usersSnap = collection.whereIn(com.google.firebase.firestore.FieldPath.documentId(), chunk).get().await()
            result.addAll(usersSnap.documents.mapNotNull { it.toObject(UsuarioDto::class.java)?.copy(id = it.id) })
        }
        return result
    }

    override suspend fun getFollowing(userId: String): List<UsuarioDto> {
        val followingIds = getFollowingIds(userId)
        
        if (followingIds.isEmpty()) return emptyList()
        
        val chunks = followingIds.chunked(10)
        val result = mutableListOf<UsuarioDto>()
        for (chunk in chunks) {
            val usersSnap = collection.whereIn(com.google.firebase.firestore.FieldPath.documentId(), chunk).get().await()
            result.addAll(usersSnap.documents.mapNotNull { it.toObject(UsuarioDto::class.java)?.copy(id = it.id) })
        }
        return result
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        val followingSnapshot = collection.document(userId).collection("following").get().await()
        return followingSnapshot.documents.map { it.id }
    }
}
