package com.example.condorapp.data.datasource

import com.example.condorapp.data.dto.UsuarioDto
import com.example.condorapp.data.remote.ApiService
import javax.inject.Inject

/**
 * DataSource remoto para usuarios vía Retrofit/API REST.
 * Implementa la interfaz UsuarioDataSource para permitir intercambio con Firestore.
 *
 * Nota: saveUsuario y updateUsuario no están soportados en el backend REST actual.
 * Si se necesitan, se deben implementar los endpoints correspondientes en el backend.
 */
class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: ApiService
) : UsuarioDataSource {

    override suspend fun getAllUsuarios(): List<UsuarioDto> {
        return apiService.getAllUsuarios()
    }

    override suspend fun getUsuarioById(id: String): UsuarioDto {
        return apiService.getUsuarioById(id.toInt())
    }

    override suspend fun saveUsuario(id: String, dto: UsuarioDto) {
        // El backend REST no tiene endpoint POST /usuarios por ahora
        throw UnsupportedOperationException("POST /usuarios no implementado en el backend REST")
    }

    override suspend fun updateUsuario(id: String, fields: Map<String, Any>) {
        // El backend REST no tiene endpoint PUT /usuarios por ahora
        throw UnsupportedOperationException("PUT /usuarios no implementado en el backend REST")
    }
}
