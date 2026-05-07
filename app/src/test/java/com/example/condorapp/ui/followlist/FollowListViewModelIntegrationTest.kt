package com.example.condorapp.ui.followlist

import androidx.lifecycle.SavedStateHandle
import com.example.condorapp.MainDispatcherRule
import com.example.condorapp.data.datasource.UsuarioDataSource
import com.example.condorapp.data.dto.UsuarioDto
import com.example.condorapp.data.repository.AuthRepository
import com.example.condorapp.data.repository.UsuarioRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** DataSource falso en memoria para pruebas de integración. */
class FakeUsuarioDataSource : UsuarioDataSource {
    val users = mutableMapOf<String, UsuarioDto>()
    val follows = mutableSetOf<Pair<String, String>>() // Pair(followerId, followingId)

    override suspend fun getAllUsuarios() = users.values.toList()
    override suspend fun getUsuarioById(id: String) = users[id] ?: throw Exception("Not found")
    override suspend fun saveUsuario(id: String, dto: UsuarioDto) { users[id] = dto }
    override suspend fun updateUsuario(id: String, fields: Map<String, Any>) {}
    
    override suspend fun toggleFollow(followerId: String, followingId: String): Boolean {
        val pair = Pair(followerId, followingId)
        return if (follows.contains(pair)) {
            follows.remove(pair)
            false
        } else {
            follows.add(pair)
            true
        }
    }

    override suspend fun isFollowing(followerId: String, followingId: String) = follows.contains(Pair(followerId, followingId))
    
    override suspend fun getFollowers(userId: String): List<UsuarioDto> {
        return follows.filter { it.second == userId }.mapNotNull { users[it.first] }
    }

    override suspend fun getFollowing(userId: String): List<UsuarioDto> {
        return follows.filter { it.first == userId }.mapNotNull { users[it.second] }
    }

    override suspend fun getFollowingIds(userId: String): List<String> {
        return follows.filter { it.first == userId }.map { it.second }
    }

    override suspend fun toggleSaveArticle(userId: String, articleId: String): Boolean {
        // Mock implementation
        val user = users[userId] ?: return false
        val isSaved = user.savedArticles.contains(articleId)
        val updatedList = if (isSaved) {
            user.savedArticles - articleId
        } else {
            user.savedArticles + articleId
        }
        users[userId] = user.copy(savedArticles = updatedList)
        return !isSaved
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class FollowListViewModelIntegrationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `loadLists integration with fake data source`() = runTest {
        // Arrange
        val fakeDataSource = FakeUsuarioDataSource()
        fakeDataSource.users["user1"] = UsuarioDto("user1", "Alice")
        fakeDataSource.users["user2"] = UsuarioDto("user2", "Bob")
        fakeDataSource.users["targetUser"] = UsuarioDto("targetUser", "Charlie")
        
        // user1 y user2 siguen a targetUser
        fakeDataSource.follows.add(Pair("user1", "targetUser"))
        fakeDataSource.follows.add(Pair("user2", "targetUser"))
        // targetUser sigue a user1
        fakeDataSource.follows.add(Pair("targetUser", "user1"))

        // El usuario actual es "currentUser" y sigue a "user2"
        fakeDataSource.users["currentUser"] = UsuarioDto("currentUser", "Me")
        fakeDataSource.follows.add(Pair("currentUser", "user2"))

        val repository = UsuarioRepository(fakeDataSource)
        
        val authRepository: AuthRepository = mockk()
        val mockUser: FirebaseUser = mockk { every { uid } returns "currentUser" }
        every { authRepository.currentUser } returns mockUser

        val savedStateHandle = SavedStateHandle(mapOf("userId" to "targetUser"))

        // Act
        val viewModel = FollowListViewModel(repository, authRepository, savedStateHandle)

        // Assert
        // Followers de targetUser deben ser 2 (user1 y user2)
        assertEquals(2, viewModel.uiState.value.followers.size)
        // Following de targetUser debe ser 1 (user1)
        assertEquals(1, viewModel.uiState.value.following.size)
        // myFollowingIds (lo que sigue el current user) debe ser 1 (user2)
        assertEquals(1, viewModel.uiState.value.myFollowingIds.size)
        assertEquals("user2", viewModel.uiState.value.myFollowingIds.first())
    }

    @Test
    fun `toggleFollow integration toggles state in fake data source`() = runTest {
        // Arrange
        val fakeDataSource = FakeUsuarioDataSource()
        fakeDataSource.users["currentUser"] = UsuarioDto("currentUser", "Me")
        fakeDataSource.users["targetUser"] = UsuarioDto("targetUser", "Charlie")
        
        val repository = UsuarioRepository(fakeDataSource)
        val authRepository: AuthRepository = mockk()
        val mockUser: FirebaseUser = mockk { every { uid } returns "currentUser" }
        every { authRepository.currentUser } returns mockUser

        val savedStateHandle = SavedStateHandle(mapOf("userId" to "targetUser"))
        val viewModel = FollowListViewModel(repository, authRepository, savedStateHandle)

        // Inicialmente no sigue
        assertEquals(false, viewModel.uiState.value.myFollowingIds.contains("targetUser"))

        // Act 1: Follow
        viewModel.toggleFollow("targetUser")
        
        // Assert 1
        assertEquals(true, viewModel.uiState.value.myFollowingIds.contains("targetUser"))
        assertEquals(true, fakeDataSource.follows.contains(Pair("currentUser", "targetUser")))

        // Act 2: Unfollow
        viewModel.toggleFollow("targetUser")

        // Assert 2
        assertEquals(false, viewModel.uiState.value.myFollowingIds.contains("targetUser"))
        assertEquals(false, fakeDataSource.follows.contains(Pair("currentUser", "targetUser")))
    }
}
