@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.map

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.condorapp.data.Review
import com.example.condorapp.ui.theme.CondorStarActive
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

/**
 * Pantalla de mapa de reviews — Sprint 13.5.
 *
 * Muestra marcadores de reviews publicados en las últimas 24 horas.
 * Al clickear un marcador se despliega un ModalBottomSheet con el detalle.
 * Usa rememberCameraPositionState para evitar recomposiciones innecesarias.
 */
@Composable
fun MapScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapScreenContent(
        state = uiState,
        modifier = modifier,
        onBack = onBack,
        onMarkerClick = viewModel::onMarkerClick,
        onDismissSheet = viewModel::onDismissSheet,
        onRefresh = viewModel::refreshReviews,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
fun MapScreenContent(
    state: MapUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onMarkerClick: (Review) -> Unit,
    onDismissSheet: () -> Unit,
    onRefresh: () -> Unit,
    onErrorShown: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Colombia central como ubicación por defecto
    val defaultLocation = LatLng(4.6097, -74.0817)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 6f)
    }

    // Estado de permisos de ubicación
    var locationPermissionGranted by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // Solicitar permisos al entrar
    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Centrar en la ubicación del usuario cuando se otorgan los permisos
    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            try {
                @SuppressLint("MissingPermission")
                val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                fusedClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(userLatLng, 12f)
                        )
                    }
                }
            } catch (_: Exception) {
                // Falla silenciosa — se queda en ubicación por defecto
            }
        }
    }

    // Mostrar error en Snackbar
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Reintentar",
                duration = SnackbarDuration.Long
            ).let { result ->
                if (result == SnackbarResult.ActionPerformed) {
                    onRefresh()
                }
            }
            onErrorShown()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Botón de refrescar reviews
                FloatingActionButton(
                    onClick = onRefresh,
                    containerColor = colorScheme.primaryContainer,
                    contentColor = colorScheme.onPrimaryContainer,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refrescar reviews")
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionGranted
                ),
                uiSettings = MapUiSettings(
                    myLocationButtonEnabled = locationPermissionGranted,
                    zoomControlsEnabled = true
                )
            ) {
                // Marcadores de reviews
                state.reviews.forEach { review ->
                    val lat = review.lat ?: return@forEach
                    val lng = review.lng ?: return@forEach
                    Marker(
                        state = MarkerState(position = LatLng(lat, lng)),
                        title = review.name,
                        snippet = "★${review.rating} — ${review.articuloNombre}",
                        onClick = {
                            onMarkerClick(review)
                            true
                        }
                    )
                }
            }

            // Botón de regreso (TopBar overlay)
            Surface(
                onClick = onBack,
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .align(Alignment.TopStart),
                shape = CircleShape,
                color = colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 6.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = colorScheme.onSurface
                    )
                }
            }

            // Badge con la cantidad de reviews
            Surface(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(20.dp),
                color = colorScheme.primaryContainer.copy(alpha = 0.95f),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.RateReview,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = colorScheme.primary
                    )
                    Text(
                        text = "${state.reviews.size} reviews en las últimas 24h",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onPrimaryContainer
                    )
                }
            }

            // Indicador de carga
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = colorScheme.primary
                )
            }
        }
    }

    // BottomSheet con detalle del review seleccionado
    state.selectedReview?.let { review ->
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            containerColor = colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            ReviewDetailSheet(review = review)
        }
    }
}

/**
 * Contenido del BottomSheet con la información detallada del review.
 */
@Composable
fun ReviewDetailSheet(
    review: Review,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // Header: Avatar + Nombre + Artículo
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = review.name.first().uppercase(),
                    color = colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorScheme.onSurface
                )
                if (review.articuloNombre.isNotBlank()) {
                    Text(
                        text = review.articuloNombre,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.outline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Calificación con estrellas
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Calificación:",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onSurface
            )
            Spacer(Modifier.width(4.dp))
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < review.rating) CondorStarActive
                    else colorScheme.outlineVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = "${review.rating}/5",
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.outline
            )
        }

        Spacer(Modifier.height(16.dp))

        // Comentario
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurface.copy(alpha = 0.85f),
                modifier = Modifier.padding(16.dp),
                lineHeight = 22.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        // Likes y metadata
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${review.likes} likes",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.outline
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Publicado desde esta ubicación",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.outline
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
