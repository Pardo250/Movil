package com.example.condorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R

data class EditProfileUiState(
    val username: String = "",
    val fullName: String = "",
    val bio: String = ""
) {
    val canOpenInterests: Boolean
        get() = username.isNotBlank() || fullName.isNotBlank() || bio.isNotBlank()

    val canSave: Boolean
        get() = username.isNotBlank() && fullName.isNotBlank()
}

@Composable
fun EditProfileBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.Olivafeed))
    ) {
        content()
    }
}

@Composable
fun EditProfileBackButton(onBack: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color(0xFF9DB38C)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun EditProfileHeader(onChangePhoto: () -> Unit = {}) {

    val avatarSize = 90.dp
    val iconSize = 45.dp
    val green = Color(0xFF0A8F3C)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(Color(0xFF2E6B3E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = null, // ✅ CORREGIDO
                tint = Color.White,
                modifier = Modifier.size(iconSize)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onChangePhoto) {
            Text(
                text = stringResource(R.string.change_profile_photo),
                color = green,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun LabeledTextField(
    labelRes: Int,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderRes: Int,
    minLines: Int = 1
) {
    Column {
        Text(
            text = stringResource(labelRes),
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(stringResource(placeholderRes)) },
            shape = RoundedCornerShape(12.dp),
            minLines = minLines,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun EditProfileButtons(
    canOpenInterests: Boolean,
    canSave: Boolean,
    onDestinationInterests: () -> Unit,
    onSaveChanges: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val green = Color(0xFF0A8F3C)
    val grayButton = Color(0xFFBDBDBD)
    val red = Color(0xFFE53935)

    Column {
        Button(
            onClick = onDestinationInterests,
            enabled = canOpenInterests,
            colors = ButtonDefaults.buttonColors(
                containerColor = grayButton,
                disabledContainerColor = grayButton
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(stringResource(R.string.destination_interests))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSaveChanges,
            enabled = canSave,
            colors = ButtonDefaults.buttonColors(containerColor = green),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text(
                text = stringResource(R.string.save_changes),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        TextButton(
            onClick = onDeleteAccount,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(R.string.delete_account),
                color = red
            )
        }
    }
}

@Composable
fun EditProfileScreenRoute(
    onBack: () -> Unit = {},
    onDestinationInterests: () -> Unit = {},
    onChangePhoto: () -> Unit = {}
) {
    var state by remember { mutableStateOf(EditProfileUiState()) } // ✅ CORREGIDO

    var messageRes by remember { mutableStateOf<Int?>(null) }

    EditProfileScreenContent(
        state = state,
        onBack = onBack,
        onChangePhoto = onChangePhoto,
        onUsernameChange = { state = state.copy(username = it) },
        onFullNameChange = { state = state.copy(fullName = it) },
        onBioChange = { state = state.copy(bio = it) },
        onDestinationInterests = onDestinationInterests,
        onSaveChanges = {
            messageRes = R.string.save_changes
        },
        onDeleteAccount = {
            state = EditProfileUiState()
            messageRes = R.string.delete_account
        },
        messageRes = messageRes,
        onDismissMessage = { messageRes = null }
    )
}

@Composable
fun EditProfileScreenContent(
    state: EditProfileUiState,
    onBack: () -> Unit,
    onChangePhoto: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onDestinationInterests: () -> Unit,
    onSaveChanges: () -> Unit,
    onDeleteAccount: () -> Unit,
    messageRes: Int?,
    onDismissMessage: () -> Unit
) {
    EditProfileBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            EditProfileBackButton(onBack = onBack)

            Spacer(modifier = Modifier.height(30.dp))

            EditProfileHeader(onChangePhoto = onChangePhoto)

            Spacer(modifier = Modifier.height(20.dp))

            LabeledTextField(
                labelRes = R.string.username_label,
                value = state.username,
                onValueChange = onUsernameChange,
                placeholderRes = R.string.value_placeholder
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledTextField(
                labelRes = R.string.full_name_label,
                value = state.fullName,
                onValueChange = onFullNameChange,
                placeholderRes = R.string.value_placeholder
            )

            Spacer(modifier = Modifier.height(16.dp))

            LabeledTextField(
                labelRes = R.string.biographic_label,
                value = state.bio,
                onValueChange = onBioChange,
                placeholderRes = R.string.value_placeholder,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(20.dp))

            EditProfileButtons(
                canOpenInterests = state.canOpenInterests,
                canSave = state.canSave,
                onDestinationInterests = onDestinationInterests,
                onSaveChanges = onSaveChanges,
                onDeleteAccount = onDeleteAccount
            )

            if (messageRes != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = stringResource(messageRes))
                        TextButton(onClick = onDismissMessage) {
                            Text(text = "OK")
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=360dp,height=800dp,dpi=420"
)
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreenRoute()
}
