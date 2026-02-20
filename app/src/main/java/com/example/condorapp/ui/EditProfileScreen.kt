package com.example.condorapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

private const val TAG = "EditProfile"

// Entidad de UI
data class EditProfileUiState(
    val username: String = "",
    val fullName: String = "",
    val bio: String = "",
    val messageRes: Int? = null
) {
    val canSave: Boolean get() = username.isNotBlank() && fullName.isNotBlank()
}

@Composable
fun EditProfileScreenRoute(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var state by remember { mutableStateOf(EditProfileUiState()) }

    EditProfileScreenContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onUsernameChange = { state = state.copy(username = it) },
        onFullNameChange = { state = state.copy(fullName = it) },
        onBioChange = { state = state.copy(bio = it) },
        onSave = {
            // Requisito: Print al guardar
            Log.d(TAG, "Guardando: ${state.username}, ${state.fullName}")
            state = state.copy(messageRes = R.string.save_changes)
        },
        onDeleteAccount = {
            Log.d(TAG, "Cuenta eliminada")
            state = EditProfileUiState(messageRes = R.string.delete_account)
        },
        onDismissMessage = { state = state.copy(messageRes = null) }
    )
}

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
    onDismissMessage: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        EditBackButton(onClick = onBack)
        Spacer(Modifier.height(24.dp))
        EditProfileHeader()
        Spacer(Modifier.height(32.dp))

        // Formularios Interactivos
        EditField(label = "Nombre de usuario", value = state.username, onValueChange = onUsernameChange)
        Spacer(Modifier.height(16.dp))
        EditField(label = "Nombre completo", value = state.fullName, onValueChange = onFullNameChange)
        Spacer(Modifier.height(16.dp))
        EditField(label = "Biografía", value = state.bio, onValueChange = onBioChange, isLong = true)

        Spacer(Modifier.height(32.dp))

        // Acciones al componente mayor
        Button(
            onClick = onSave,
            enabled = state.canSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Cambios", fontWeight = FontWeight.Bold)
        }

        TextButton(
            onClick = onDeleteAccount,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text("Eliminar cuenta", color = MaterialTheme.colorScheme.error)
        }

        // Feedback visual (Snackbar/Card)
        if (state.messageRes != null) {
            Spacer(Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(state.messageRes), modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismissMessage) { Text("OK") }
                }
            }
        }
    }
}

@Composable
fun EditBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = null)
    }
}

@Composable
fun EditProfileHeader(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxWidth()) {
        Box {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Icon(painter = painterResource(R.drawable.ic_back), contentDescription = null, modifier = Modifier.padding(6.dp), tint = Color.White)
            }
        }
        TextButton(onClick = {}) {
            Text("Cambiar foto de perfil", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun EditField(label: String, value: String, onValueChange: (String) -> Unit, isLong: Boolean = false) {
    Column {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            minLines = if (isLong) 3 else 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    CondorappTheme { EditProfileScreenRoute() }
}