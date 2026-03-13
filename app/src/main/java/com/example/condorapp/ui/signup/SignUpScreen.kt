package com.example.condorapp.ui.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de registro. Conecta el SignUpViewModel con el contenido
 * stateless.
 */
@Composable
fun SignUpScreenRoute(
        modifier: Modifier = Modifier,
        viewModel: SignUpViewModel = viewModel(),
        onRegisterSuccess: () -> Unit = {},
        onCancel: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSignUpSuccessful) {
        if (uiState.isSignUpSuccessful) {
            onRegisterSuccess()
        }
    }

    SignUpScreenContent(
            state = uiState,
            modifier = modifier,
            onNameChange = viewModel::onNameChange,
            onLastNameChange = viewModel::onLastNameChange,
            onUsernameChange = viewModel::onUsernameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
            onRegisterClick = viewModel::onSignUp,
            onCancelClick = onCancel
    )
}

/** Contenido stateless de la pantalla de registro. */
@Composable
fun SignUpScreenContent(
        state: SignUpUiState,
        modifier: Modifier = Modifier,
        onNameChange: (String) -> Unit,
        onLastNameChange: (String) -> Unit,
        onUsernameChange: (String) -> Unit,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onConfirmPasswordChange: (String) -> Unit,
        onRegisterClick: () -> Unit,
        onCancelClick: () -> Unit,
        onDismissMessage: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
            modifier =
                    modifier.fillMaxSize()
                            .background(colorScheme.background)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        SignUpHeader()
        Spacer(modifier = Modifier.height(24.dp))
        SignUpForm(
                state = state,
                onNameChange = onNameChange,
                onLastNameChange = onLastNameChange,
                onUsernameChange = onUsernameChange,
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onConfirmPasswordChange = onConfirmPasswordChange
        )
        Spacer(modifier = Modifier.height(32.dp))
        SignUpActions(
                canSignUp = state.canSignUp,
                onRegisterClick = onRegisterClick,
                onCancelClick = onCancelClick
        )
        if (state.messageRes != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(state.messageRes), 
                        color = colorScheme.onErrorContainer
                    )
                    TextButton(onClick = onDismissMessage) {
                        Text(text = "OK", color = colorScheme.onErrorContainer)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/** Header del registro con logo y texto descriptivo. */
@Composable
fun SignUpHeader(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
        )
        Text(
                text = "condorapp",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
        )
        Text(
                text = "Descubre la magia de los\nAndes y más allá",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

/** Formulario de registro con todos los campos. */
@Composable
fun SignUpForm(
        state: SignUpUiState,
        onNameChange: (String) -> Unit,
        onLastNameChange: (String) -> Unit,
        onUsernameChange: (String) -> Unit,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onConfirmPasswordChange: (String) -> Unit,
        modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SignUpTextField(
                    label = "Nombre",
                    value = state.name,
                    onValueChange = onNameChange,
                    modifier = Modifier.weight(1f)
            )
            SignUpTextField(
                    label = "Apellido",
                    value = state.lastName,
                    onValueChange = onLastNameChange,
                    modifier = Modifier.weight(1f)
            )
        }
        SignUpTextField(
                label = "Username",
                value = state.username,
                onValueChange = onUsernameChange
        )
        SignUpTextField(label = "Correo", value = state.email, onValueChange = onEmailChange)
        SignUpTextField(
                label = "Contraseña",
                value = state.password,
                onValueChange = onPasswordChange,
                isPassword = true
        )
        SignUpTextField(
                label = "Confirmar Contraseña",
                value = state.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                isPassword = true
        )
    }
}

/** Campo de texto reutilizable para el formulario de registro. */
@Composable
fun SignUpTextField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        isPassword: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier) {
        Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation =
                        if (isPassword) PasswordVisualTransformation()
                        else VisualTransformation.None,
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorScheme.primary,
                                unfocusedBorderColor = colorScheme.outline,
                                unfocusedContainerColor =
                                        colorScheme.surfaceVariant.copy(alpha = 0.2f),
                                focusedContainerColor =
                                        colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        )
        )
    }
}

/** Botones de acción: registrarse y cancelar. */
@Composable
fun SignUpActions(
        canSignUp: Boolean,
        onRegisterClick: () -> Unit,
        onCancelClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
                onClick = onRegisterClick,
                enabled = canSignUp,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
        ) { Text("Registrarse", fontWeight = FontWeight.Bold) }
        OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
        ) { Text("Cancelar", color = MaterialTheme.colorScheme.onBackground) }
    }
}

@Preview(showSystemUi = true, name = "SignUp - Light")
@Composable
fun SignUpScreenLightPreview() {
    CondorappTheme(darkTheme = false) { SignUpScreenRoute() }
}

@Preview(showSystemUi = true, name = "SignUp - Dark")
@Composable
fun SignUpScreenDarkPreview() {
    CondorappTheme(darkTheme = true) { SignUpScreenRoute() }
}
