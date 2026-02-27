@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
            Log.d(TAG, "Guardando: ${state.username}")
            state = state.copy(messageRes = R.string.save_changes)
        },
        onDeleteAccount = {
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
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Botón atrás circular
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

        Spacer(Modifier.height(24.dp))
        EditProfileHeader()
        Spacer(Modifier.height(32.dp))

        EditField(labelRes = R.string.username_label, value = state.username, onValueChange = onUsernameChange)
        Spacer(Modifier.height(16.dp))
        EditField(labelRes = R.string.full_name_label, value = state.fullName, onValueChange = onFullNameChange)
        Spacer(Modifier.height(16.dp))
        EditField(labelRes = R.string.biographic_label, value = state.bio, onValueChange = onBioChange, isLong = true)

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onSave,
            enabled = state.canSave,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            )
        ) {
            Text(stringResource(R.string.save_changes), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        TextButton(
            onClick = onDeleteAccount,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.delete_account), color = colorScheme.error)
        }

        // Feedback Card
        state.messageRes?.let {
            Card(
                modifier = Modifier.padding(top = 20.dp),
                colors = CardDefaults.cardColors(
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

@Composable
fun EditField(labelRes: Int, value: String, onValueChange: (String) -> Unit, isLong: Boolean = false) {
    val colorScheme = MaterialTheme.colorScheme
    Column {
        Text(
            stringResource(labelRes), 
            fontWeight = FontWeight.Bold, 
            color = colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            minLines = if (isLong) 3 else 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline
            )
        )
    }
}

@Composable
fun EditProfileHeader() {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
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
    CondorappTheme(darkTheme = false) {
        EditProfileScreenRoute()
    }
}

@Preview(showBackground = true, name = "Edit Profile - Dark")
@Composable
fun EditProfilePreviewDark() {
    CondorappTheme(darkTheme = true) {
        EditProfileScreenRoute()
    }
}
