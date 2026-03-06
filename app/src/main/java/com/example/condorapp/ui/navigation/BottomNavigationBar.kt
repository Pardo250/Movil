package com.example.condorapp.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R

/**
 * Barra de navegación inferior flotante con diseño personalizado. Se muestra en las pantallas
 * principales (Home, Explore, Profile).
 */
@Composable
fun BottomFloatingBar(
        currentRoute: String?,
        onNavigate: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier.fillMaxWidth().padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
    ) {
        Card(
                modifier = Modifier.padding(horizontal = 20.dp).height(80.dp),
                shape = RoundedCornerShape(40.dp),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationItem(
                        iconRes = R.drawable.ic_home,
                        label = "Home",
                        route = Screen.Home.route,
                        isSelected = currentRoute == Screen.Home.route,
                        onClick = { onNavigate(Screen.Home.route) }
                )
                NavigationItem(
                        iconRes = R.drawable.ic_explore,
                        label = "Explore",
                        route = Screen.Explore.route,
                        isSelected = currentRoute == Screen.Explore.route,
                        onClick = { onNavigate(Screen.Explore.route) }
                )
                NavigationItem(
                        iconRes = R.drawable.ic_profile,
                        label = "Profile",
                        route = Screen.Profile.route,
                        isSelected = currentRoute == Screen.Profile.route,
                        onClick = { onNavigate(Screen.Profile.route) }
                )
            }
        }
    }
}

/**
 * Componente individual de un ítem de la barra de navegación. Muestra un ícono con texto y estado
 * seleccionado.
 */
@Composable
fun NavigationItem(
        iconRes: Int,
        label: String,
        route: String,
        isSelected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier =
                    modifier.clip(RoundedCornerShape(20.dp)).clickable { onClick() }.padding(8.dp)
    ) {
        Box(
                modifier =
                        Modifier.size(48.dp)
                                .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                        else Color.Transparent,
                                        RoundedCornerShape(24.dp)
                                ),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    painter = painterResource(iconRes),
                    contentDescription = label,
                    tint =
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
            )
        }
        Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color =
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
