package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.UsuarioRemoteDataSource
import com.example.condorapp.data.dto.UsuarioDto
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val remoteDataSource: UsuarioRemoteDataSource
) {
    suspend fun getAllUsuarios(): List<UsuarioDto> {
        return remoteDataSource.getAllUsuarios()
    }

    suspend fun getUsuarioById(id: Int): UsuarioDto {
        return remoteDataSource.getUsuarioById(id)
    }
}
