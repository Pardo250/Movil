package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.UsuarioDataSource
import com.example.condorapp.data.dto.UsuarioDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UsuarioRepositoryTest {

    private val dataSource: UsuarioDataSource = mockk()
    private val repository = UsuarioRepository(dataSource)

    @Test
    fun `getAllUsuarios returns list of UserInfo on success`() = runTest {
        // Arrange
        val dtoList = listOf(UsuarioDto(id = "1", nombre = "Juan"))
        coEvery { dataSource.getAllUsuarios() } returns dtoList

        // Act
        val result = repository.getAllUsuarios()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals("Juan", result.getOrNull()?.first()?.nombre)
    }

    @Test
    fun `getAllUsuarios returns failure on error`() = runTest {
        // Arrange
        coEvery { dataSource.getAllUsuarios() } throws Exception("Network error")

        // Act
        val result = repository.getAllUsuarios()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getUsuarioById returns mapped UserInfo`() = runTest {
        // Arrange
        val dto = UsuarioDto(id = "123", nombre = "Maria", email = "maria@test.com")
        coEvery { dataSource.getUsuarioById("123") } returns dto

        // Act
        val result = repository.getUsuarioById("123")

        // Assert
        assertTrue(result.isSuccess)
        val userInfo = result.getOrNull()
        assertEquals("Maria", userInfo?.nombre)
        assertEquals("maria@test.com", userInfo?.email)
    }

    @Test
    fun `saveUsuario calls dataSource and returns success`() = runTest {
        // Arrange
        coEvery { dataSource.saveUsuario(any(), any()) } returns Unit

        // Act
        val result = repository.saveUsuario("uid", "Jose", "jose@t.com", "jose123")

        // Assert
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { dataSource.saveUsuario("uid", any()) }
    }
}
