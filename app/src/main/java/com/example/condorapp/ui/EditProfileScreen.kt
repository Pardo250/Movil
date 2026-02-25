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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

private const val TAG = "EditProfile"

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
            println("Guardando perfil -> username: ${state.username}, fullName: ${state.fullName}, bio: ${state.bio}")
            Log.d(TAG, "Guardando: ${state.username}, ${state.fullName}")
            state = state.copy(messageRes = R.string.save_changes)
        },
        onDeleteAccount = {
            println("Cuenta eliminada para usuario: ${state.username}")
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
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        EditBackButton(onClick = onBack)

        Spacer(Modifier.height(24.dp))

        EditProfileHeader()

        Spacer(Modifier.height(32.dp))

        // ✅ Formularios (labels desde strings)
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

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onSave,
            enabled = state.canSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.save_changes),
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = onDeleteAccount,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.delete_account),
                color = cs.error
            )
        }

        // ✅ Feedback (se ve bien en dark)
        if (state.messageRes != null) {
            Spacer(Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = cs.secondaryContainer)
            ) {
                Row(
                    Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(state.messageRes),
                        modifier = Modifier.weight(1f),
                        color = cs.onSecondaryContainer
                    )
                    TextButton(onClick = onDismissMessage) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
fun EditBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    IconButton(
        onClick = onClick,
        modifier = modifier.background(cs.surfaceVariant, CircleShape)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
            tint = cs.onSurfaceVariant
        )
    }
}

@Composable
fun EditProfileHeader(modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Box {
            Image(
                painter = painterResource(R.drawable.avatar),
                contentDescription = stringResource(R.string.cd_profile_photo),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )

            // Botoncito de “cámara” (o icon)
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(32.dp),
                shape = CircleShape,
                color = cs.primary
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.cd_change_photo),
                    modifier = Modifier.padding(6.dp),
                    tint = cs.onPrimary // ✅ en dark no se pierde
                )
            }
        }

        TextButton(onClick = {}) {
            Text(
                text = stringResource(R.string.change_profile_photo),
                color = cs.primary
            )
        }
    }
}

@Composable
fun EditField(
    labelRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    isLong: Boolean = false,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelLarge,
            color = cs.primary
        )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileLightPreview() {
    CondorappTheme(darkTheme = false) { EditProfileScreenRoute() }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileDarkPreview() {
    CondorappTheme(darkTheme = true) { EditProfileScreenRoute() }
}