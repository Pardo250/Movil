package com.example.condorapp.ui.notifications

import androidx.compose.foundation.background
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.condorapp.R
import com.example.condorapp.data.Notification
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de notificaciones. Conecta el NotificationsViewModel con el
 * contenido stateless.
 */
@Composable
fun NotificationsScreenRoute(
        modifier: Modifier = Modifier,
        viewModel: NotificationsViewModel = hiltViewModel(),
        onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreenContent(
            state = uiState,
            modifier = modifier,
            onBack = onBack,
            onTabSelected = viewModel::onTabSelected,
            onClearAll = viewModel::onClearAll
    )
}

/** Contenido stateless de la pantalla de notificaciones. */
@Composable
fun NotificationsScreenContent(
        state: NotificationsUiState,
        modifier: Modifier = Modifier,
        onBack: () -> Unit,
        onTabSelected: (String) -> Unit,
        onClearAll: () -> Unit
) {
    // Filtrar notificaciones según el tab seleccionado
    val filtered = when (state.selectedTab) {
        "Likes"    -> state.notifications.filter { it.type == "like" }
        "Followers" -> state.notifications.filter { it.type == "follow" }
        else       -> state.notifications // "Todo"
    }

    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Spacer(modifier = Modifier.height(20.dp))
        NotificationTopBar(onBack = onBack, onClearAll = onClearAll)
        Spacer(modifier = Modifier.height(20.dp))
        NotificationTabs(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected,
                modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    "No tienes notificaciones",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.weight(1f)
            ) {
                items(filtered) { notification ->
                    NotificationItem(notification = notification)
                }
            }
        }
    }
}

/** Barra superior de notificaciones con botón atrás y limpiar todo. */
@Composable
fun NotificationTopBar(modifier: Modifier = Modifier, onBack: () -> Unit, onClearAll: () -> Unit) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(40.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
                text = "Notificaciones",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = onClearAll) {
            Text(
                    text = "Limpiar todo",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 14.sp
            )
        }
    }
}

/** Tabs de filtros de notificaciones. */
@Composable
fun NotificationTabs(
        selectedTab: String,
        onTabSelected: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    Row(
            modifier =
                    modifier.fillMaxWidth()
                            .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(30.dp)
                            )
                            .padding(4.dp)
    ) {
        val tabs = listOf("Todo", "Likes", "Followers")
        tabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Box(
                    modifier =
                            Modifier.weight(1f)
                                    .background(
                                            color =
                                                    if (isSelected)
                                                            MaterialTheme.colorScheme.primary
                                                    else MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(30.dp)
                                    )
                                    .clip(RoundedCornerShape(30.dp))
                                    .clickable { onTabSelected(tab) }
                                    .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = tab,
                        color =
                                if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/** Ítem individual de notificación. */
@Composable
fun NotificationItem(notification: Notification, modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Box(
                modifier =
                        Modifier.size(70.dp)
                                .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(16.dp)
                                ),
                contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                    model = notification.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(48.dp).clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                        text = notification.userName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                        text = notification.action,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                    onClick = {},
                    shape = RoundedCornerShape(50.dp),
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                            ),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
            ) {
                Text(
                        text = "Seguir También",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                    text = notification.time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview(showSystemUi = true, name = "Notifications - Light")
@Composable
fun NotificationsScreenLightPreview() {
    CondorappTheme(darkTheme = false) { NotificationsScreenRoute() }
}

@Preview(showSystemUi = true, name = "Notifications - Dark")
@Composable
fun NotificationsScreenDarkPreview() {
    CondorappTheme(darkTheme = true) { NotificationsScreenRoute() }
}
