package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto
import com.google.firebase.firestore.FirebaseFirestore
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
}
