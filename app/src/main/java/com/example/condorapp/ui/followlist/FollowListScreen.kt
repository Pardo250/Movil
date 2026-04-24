@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.followlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.condorapp.data.UserInfo

@Composable
fun FollowListScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: FollowListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FollowListScreenContent(
        state = uiState,
        modifier = modifier,
        onBackClick = onBackClick,
        onTabSelected = viewModel::onTabSelected,
        onFollowToggle = viewModel::toggleFollow,
        onUserClick = onUserClick
    )
}

@Composable
fun FollowListScreenContent(
    state: FollowListUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onFollowToggle: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Conexiones", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.background
            )
        )

        // Tabs
        TabRow(
            selectedTabIndex = state.selectedTab,
            containerColor = colorScheme.background,
            contentColor = colorScheme.primary
        ) {
            Tab(
                selected = state.selectedTab == 0,
                onClick = { onTabSelected(0) },
                text = { Text("Seguidores (${state.followers.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = state.selectedTab == 1,
                onClick = { onTabSelected(1) },
                text = { Text("Siguiendo (${state.following.size})", fontWeight = FontWeight.Bold) }
            )
        }

        // List Content
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorScheme.primary)
            }
        } else if (state.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.errorMessage, color = colorScheme.error)
            }
        } else {
            val list = if (state.selectedTab == 0) state.followers else state.following
            if (list.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay usuarios en esta lista.", color = colorScheme.outline)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp)
                ) {
                    items(list) { user ->
                        UserListItem(
                            user = user,
                            isFollowing = state.myFollowingIds.contains(user.id),
                            onFollowToggle = { onFollowToggle(user.id) },
                            onClick = { onUserClick(user.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    user: UserInfo,
    isFollowing: Boolean,
    onFollowToggle: () -> Unit,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(colorScheme.primary.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.nombre.firstOrNull()?.uppercase() ?: "?",
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Info
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.nombre,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground,
                fontSize = 16.sp
            )
            Text(
                text = user.email,
                color = colorScheme.outline,
                fontSize = 12.sp
            )
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Botón Seguir/Siguiendo
        Button(
            onClick = onFollowToggle,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFollowing) colorScheme.surfaceVariant else colorScheme.primary,
                contentColor = if (isFollowing) colorScheme.onSurfaceVariant else colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            modifier = Modifier.height(32.dp)
        ) {
            Text(
                text = if (isFollowing) "Siguiendo" else "Seguir",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
