@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.condorapp.ui.editprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de edición de perfil. Conecta el EditProfileViewModel con el
 * contenido stateless.
 */
@Composable
fun EditProfileScreenRoute(
        modifier: Modifier = Modifier,
        viewModel: EditProfileViewModel = hiltViewModel(),
        onBack: () -> Unit = {},
        onSignOut: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navega a Login cuando Firebase cierra la sesión exitosamente
    LaunchedEffect(uiState.isSignedOut) {
        if (uiState.isSignedOut) {
            onSignOut()
        }
    }

    EditProfileScreenContent(
            state = uiState,
            modifier = modifier,
            onBack = onBack,
            onUsernameChange = viewModel::onUsernameChange,
            onFullNameChange = viewModel::onFullNameChange,
            onBioChange = viewModel::onBioChange,
            onSave = viewModel::onSave,
            onDeleteAccount = viewModel::onDeleteAccount,
            onSignOut = viewModel::onSignOut,
            onDismissMessage = viewModel::onDismissMessage
    )
}

/** Contenido stateless de la pantalla de edición de perfil. */
@Composable
fun EditProfileScreenContent(
        state: EditProfileUiState,
        modifier: Modifier = Modifier,
        onBack: () -> Unit,
        onUsernameChange: (String) -> Unit,
        onFullNameChange: (String) -> Unit,
        onBioChange: (String) -> Unit,
        onSave: () -> Unit,
        onDeleteAccount: () -> Unit,
        onSignOut: () -> Unit,
        onDismissMessage: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
            modifier =
                    modifier.fillMaxSize()
                            .background(colorScheme.background)
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
    ) {
        // Fila superior: botón atrás + botón cerrar sesión
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                    onClick = onBack,
                    modifier = Modifier.background(colorScheme.surface, CircleShape)
            ) {
                Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = colorScheme.onSurface
                )
            }

            // Botón de cerrar sesión
            TextButton(
                onClick = onSignOut
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.sign_out),
                    tint = colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.sign_out),
                    color = colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(24.dp))
        EditProfileHeader()
        Spacer(Modifier.height(32.dp))

        EditField(
                labelRes = R.string.username_label,
                value = state.username,
                onValueChange = onUsernameChange
        )
        Spacer(Modifier.height(16.dp))
        EditField(
                labelRes = R.string.full_name_label,
                value = state.fullName,
                onValueChange = onFullNameChange
        )
        Spacer(Modifier.height(16.dp))
        EditField(
                labelRes = R.string.biographic_label,
                value = state.bio,
                onValueChange = onBioChange,
                isLong = true
        )

        Spacer(Modifier.height(40.dp))

        Button(
                onClick = onSave,
                enabled = state.canSave,
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = colorScheme.primary,
                                contentColor = colorScheme.onPrimary
                        )
        ) {
            Text(
                    stringResource(R.string.save_changes),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
            )
        }

        TextButton(
                onClick = onDeleteAccount,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        ) { Text(stringResource(R.string.delete_account), color = colorScheme.error) }

        // Feedback Card
        state.messageRes?.let {
            Card(
                    modifier = Modifier.padding(top = 20.dp),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = colorScheme.secondaryContainer,
                                    contentColor = colorScheme.onSecondaryContainer
                            )
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(it), Modifier.weight(1f))
                    TextButton(onClick = onDismissMessage) {
                        Text("OK", color = colorScheme.primary)
                    }
                }
            }
        }
    }
}

/** Campo de edición reutilizable con label y estilo del tema. */
@Composable
fun EditField(
        labelRes: Int,
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        isLong: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier) {
        Text(stringResource(labelRes), fontWeight = FontWeight.Bold, color = colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                minLines = if (isLong) 3 else 1,
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorScheme.primary,
                                unfocusedBorderColor = colorScheme.outline
                        )
        )
    }
}

/** Header de edición de perfil con avatar y opción de cambiar foto. */
@Composable
fun EditProfileHeader(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier.size(100.dp).clip(CircleShape)
        )
        TextButton(onClick = {}) {
            Text(
                    stringResource(R.string.change_profile_photo),
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, name = "Edit Profile - Light")
@Composable
fun EditProfilePreviewLight() {
    CondorappTheme(darkTheme = false) { EditProfileScreenRoute() }
}

@Preview(showBackground = true, name = "Edit Profile - Dark")
@Composable
fun EditProfilePreviewDark() {
    CondorappTheme(darkTheme = true) { EditProfileScreenRoute() }
}
