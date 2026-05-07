package com.example.condorapp.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import com.example.condorapp.MainDispatcherRule
import com.example.condorapp.data.UserInfo
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import com.example.condorapp.data.repository.UsuarioRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val usuarioRepository: UsuarioRepository = mockk()
    private val reviewRepository: ReviewRepository = mockk()
    private val authRepository: AuthRepository = mockk()
    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("userId" to "targetUser"))

    @Test
    fun `loadUserProfile loads user data successfully`() = runTest {
        // Arrange
        val user = UserInfo("targetUser", "Juan", "j@j.com", followersCount = 10)
        coEvery { usuarioRepository.getUsuarioById("targetUser") } returns Result.success(user)
        coEvery { reviewRepository.getReviewsByUsuario("targetUser") } returns Result.success(emptyList())
        
        val mockUser: FirebaseUser = mockk { every { uid } returns "currentUser" }
        every { authRepository.currentUser } returns mockUser
        coEvery { usuarioRepository.isFollowing("currentUser", "targetUser") } returns Result.success(true)

        // Act
        val viewModel = UserProfileViewModel(usuarioRepository, reviewRepository, authRepository, savedStateHandle)

        // Assert
        assertEquals("Juan", viewModel.uiState.value.user?.nombre)
        assertEquals(10, viewModel.uiState.value.followersCount)
        assertEquals(true, viewModel.uiState.value.isFollowing)
    }

    @Test
    fun `loadUserProfile sets error on failure`() = runTest {
        // Arrange
        coEvery { usuarioRepository.getUsuarioById("targetUser") } returns Result.failure(Exception("Not found"))
        coEvery { reviewRepository.getReviewsByUsuario("targetUser") } returns Result.success(emptyList())
        every { authRepository.currentUser } returns null

        // Act
        val viewModel = UserProfileViewModel(usuarioRepository, reviewRepository, authRepository, savedStateHandle)

        // Assert
        assertEquals("Not found", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `toggleFollow increments follower count on success`() = runTest {
        // Arrange
        val user = UserInfo("targetUser", "Juan", "j@j.com", followersCount = 10)
        coEvery { usuarioRepository.getUsuarioById("targetUser") } returns Result.success(user)
        coEvery { reviewRepository.getReviewsByUsuario("targetUser") } returns Result.success(emptyList())
        
        val mockUser: FirebaseUser = mockk { every { uid } returns "currentUser" }
        every { authRepository.currentUser } returns mockUser
        // initially not following
        coEvery { usuarioRepository.isFollowing("currentUser", "targetUser") } returns Result.success(false)
        
        // toggle returns true (now following)
        coEvery { usuarioRepository.toggleFollow("currentUser", "targetUser") } returns Result.success(true)

        val viewModel = UserProfileViewModel(usuarioRepository, reviewRepository, authRepository, savedStateHandle)

        // Act
        viewModel.toggleFollow()

        // Assert
        assertEquals(true, viewModel.uiState.value.isFollowing)
        assertEquals(11, viewModel.uiState.value.followersCount)
    }

    @Test
    fun `toggleFollow rolls back on failure`() = runTest {
        // Arrange
        val user = UserInfo("targetUser", "Juan", "j@j.com", followersCount = 10)
        coEvery { usuarioRepository.getUsuarioById("targetUser") } returns Result.success(user)
        coEvery { reviewRepository.getReviewsByUsuario("targetUser") } returns Result.success(emptyList())
        
        val mockUser: FirebaseUser = mockk { every { uid } returns "currentUser" }
        every { authRepository.currentUser } returns mockUser
        coEvery { usuarioRepository.isFollowing("currentUser", "targetUser") } returns Result.success(false)
        
        // toggle fails
        coEvery { usuarioRepository.toggleFollow("currentUser", "targetUser") } returns Result.failure(Exception("Fail"))

        val viewModel = UserProfileViewModel(usuarioRepository, reviewRepository, authRepository, savedStateHandle)

        // Act
        viewModel.toggleFollow()

        // Assert
        // Rolls back to false and 10
        assertEquals(false, viewModel.uiState.value.isFollowing)
        assertEquals(10, viewModel.uiState.value.followersCount)
    }
}
