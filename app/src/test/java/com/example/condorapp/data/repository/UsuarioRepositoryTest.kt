package com.example.condorapp.data.repository

import com.example.condorapp.data.datasource.UsuarioDataSource
import com.example.condorapp.data.dto.UsuarioDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UsuarioRepositoryTest {

    private val dataSource: UsuarioDataSource = mockk()
    private val repository = UsuarioRepository(dataSource)

    // ─── Tests existentes migrados a Truth ──────────────────────

    @Test
    fun `getAllUsuarios returns list of UserInfo on success`() = runTest {
        // Arrange
        val dtoList = listOf(UsuarioDto(id = "1", nombre = "Juan"))
        coEvery { dataSource.getAllUsuarios() } returns dtoList

        // Act
        val result = repository.getAllUsuarios()

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.first()?.nombre).isEqualTo("Juan")
    }

    @Test
    fun `getAllUsuarios returns failure on error`() = runTest {
        // Arrange
        coEvery { dataSource.getAllUsuarios() } throws Exception("Network error")

        // Act
        val result = repository.getAllUsuarios()

        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network error")
    }

    @Test
    fun `getUsuarioById returns mapped UserInfo`() = runTest {
        // Arrange
        val dto = UsuarioDto(id = "123", nombre = "Maria", email = "maria@test.com")
        coEvery { dataSource.getUsuarioById("123") } returns dto

        // Act
        val result = repository.getUsuarioById("123")

        // Assert
        assertThat(result.isSuccess).isTrue()
        val userInfo = result.getOrNull()
        assertThat(userInfo?.nombre).isEqualTo("Maria")
        assertThat(userInfo?.email).isEqualTo("maria@test.com")
    }

    @Test
    fun `saveUsuario calls dataSource and returns success`() = runTest {
        // Arrange
        coEvery { dataSource.saveUsuario(any(), any()) } returns Unit

        // Act
        val result = repository.saveUsuario("uid", "Jose", "jose@t.com", "jose123")

        // Assert
        assertThat(result.isSuccess).isTrue()
        coVerify(exactly = 1) { dataSource.saveUsuario("uid", any()) }
    }

    // ─── Tests de mapeo DTO → Modelo (Sprint 13 req.) ───────────

    @Test
    fun `getUsuarioById maps empty bio to default Sin biografia`() = runTest {
        // Arrange — DTO con bio vacío
        val dto = UsuarioDto(id = "u1", nombre = "Test", email = "t@t.com", bio = "")
        coEvery { dataSource.getUsuarioById("u1") } returns dto

        // Act
        val result = repository.getUsuarioById("u1")

        // Assert — Mapper debe transformar "" → "Sin biografía"
        assertThat(result.getOrNull()?.bio).isEqualTo("Sin biografía")
    }

    @Test
    fun `getUsuarioById maps empty username to generated username`() = runTest {
        // Arrange — DTO con username vacío
        val dto = UsuarioDto(id = "u2", nombre = "Carlos Pérez", email = "c@t.com", username = "")
        coEvery { dataSource.getUsuarioById("u2") } returns dto

        // Act
        val result = repository.getUsuarioById("u2")

        // Assert — Mapper debe generar username como @nombre_lowercase
        val userInfo = result.getOrNull()
        assertThat(userInfo?.username).startsWith("@")
        assertThat(userInfo?.username).contains("carlos")
    }

    @Test
    fun `toggleFollow returns success with true when now following`() = runTest {
        // Arrange
        coEvery { dataSource.toggleFollow("a", "b") } returns true

        // Act
        val result = repository.toggleFollow("a", "b")

        // Assert
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isTrue()
    }

    @Test
    fun `toggleFollow returns failure on exception`() = runTest {
        // Arrange
        coEvery { dataSource.toggleFollow("a", "b") } throws Exception("Network fail")

        // Act
        val result = repository.toggleFollow("a", "b")

        // Assert
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo("Network fail")
    }
}
