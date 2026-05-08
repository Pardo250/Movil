package com.example.condorapp.ui.userprofile

import androidx.lifecycle.SavedStateHandle
import com.example.condorapp.MainDispatcherRule
import com.example.condorapp.data.Review
import com.example.condorapp.data.UserInfo
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.ReviewRepository
import com.example.condorapp.data.repository.UsuarioRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
        assertThat(viewModel.uiState.value.user?.nombre).isEqualTo("Juan")
        assertThat(viewModel.uiState.value.followersCount).isEqualTo(10)
        assertThat(viewModel.uiState.value.isFollowing).isTrue()
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
        assertThat(viewModel.uiState.value.errorMessage).isEqualTo("Not found")
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
        assertThat(viewModel.uiState.value.isFollowing).isTrue()
        assertThat(viewModel.uiState.value.followersCount).isEqualTo(11)
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

        // Assert — Rolls back to false and 10
        assertThat(viewModel.uiState.value.isFollowing).isFalse()
        assertThat(viewModel.uiState.value.followersCount).isEqualTo(10)
    }

    @Test
    fun `loadUserProfile loads reviews successfully`() = runTest {
        // Arrange
        val user = UserInfo("targetUser", "Ana", "ana@t.com")
        val reviews = listOf(
            Review("r1", "Ana", 5, "Gran lugar", 3, usuarioId = "targetUser"),
            Review("r2", "Ana", 4, "Bonito", 1, usuarioId = "targetUser")
        )
        coEvery { usuarioRepository.getUsuarioById("targetUser") } returns Result.success(user)
        coEvery { reviewRepository.getReviewsByUsuario("targetUser") } returns Result.success(reviews)
        every { authRepository.currentUser } returns null

        // Act
        val viewModel = UserProfileViewModel(usuarioRepository, reviewRepository, authRepository, savedStateHandle)

        // Assert
        assertThat(viewModel.uiState.value.reviews).hasSize(2)
        assertThat(viewModel.uiState.value.reviews.map { it.comment }).containsExactly("Gran lugar", "Bonito")
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }
}
