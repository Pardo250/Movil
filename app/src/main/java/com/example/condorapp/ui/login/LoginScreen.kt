package com.example.condorapp.ui.login

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

/**
 * Composable Route para la pantalla de Login. Conecta el LoginViewModel con el contenido stateless.
 */
@Composable
fun LoginScreenRoute(
        modifier: Modifier = Modifier,
        viewModel: LoginViewModel = viewModel(),
        onSignInSuccess: () -> Unit,
        onGoRegister: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreenContent(
            modifier = modifier,
            state = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onForgotPassword = viewModel::onForgotPassword,
            onSignIn = {
                if (viewModel.onSignIn()) {
                    onSignInSuccess()
                }
            },
            onContinueGoogle = viewModel::onContinueGoogle,
            onRegister = {
                viewModel.onRegister()
                onGoRegister()
            },
            onDismissMessage = viewModel::onDismissMessage
    )
}

/** Fondo de la pantalla de login con el color del tema. */
@Composable
fun LoginBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        content()
    }
}

/** Header con logo y subtítulo de la pantalla de login. */
@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
                painter = painterResource(R.drawable.logo2),
                contentDescription = stringResource(R.string.cd_logo_condorapp),
                modifier = Modifier.size(230.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
                text = stringResource(R.string.login_subtitle),
                fontSize = 22.sp,
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
        )
    }
}

/** Campo de texto para email con label y estilo del tema. */
@Composable
fun EmailField(modifier: Modifier = Modifier, email: String, onEmailChange: (String) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier) {
        Text(
                text = stringResource(R.string.email_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.email_placeholder)) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorScheme.primary,
                                unfocusedBorderColor = colorScheme.outline
                        )
        )
    }
}

/** Campo de texto para contraseña con máscara y estilo del tema. */
@Composable
fun PasswordField(
        modifier: Modifier = Modifier,
        password: String,
        onPasswordChange: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = modifier) {
        Text(
                text = stringResource(R.string.password_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.value_placeholder)) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors =
                        OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorScheme.primary,
                                unfocusedBorderColor = colorScheme.outline
                        )
        )
    }
}

/** Botón primario reutilizable con texto de recurso string. */
@Composable
fun PrimaryButton(
        modifier: Modifier = Modifier,
        textRes: Int,
        onClick: () -> Unit,
        enabled: Boolean = true
) {
    Button(
            onClick = onClick,
            enabled = enabled,
            modifier = modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(18.dp)
    ) { Text(text = stringResource(textRes), fontWeight = FontWeight.Bold) }
}

/** Sección divisoria con línea y texto "o". */
@Composable
fun DividerSection(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = colorScheme.outline)
        Text(
                text = stringResource(R.string.or),
                modifier = Modifier.padding(horizontal = 16.dp),
                color = colorScheme.onBackground.copy(alpha = 0.7f)
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = colorScheme.outline)
    }
}

/** Formulario de login con todos los campos y botones. */
@Composable
fun LoginForm(
        modifier: Modifier = Modifier,
        state: LoginUiState,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onForgotPassword: () -> Unit,
        onSignIn: () -> Unit,
        onContinueGoogle: () -> Unit,
        onRegister: () -> Unit,
        onDismissMessage: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        EmailField(email = state.email, onEmailChange = onEmailChange)
        Spacer(modifier = Modifier.height(24.dp))
        PasswordField(password = state.password, onPasswordChange = onPasswordChange)
        Spacer(modifier = Modifier.height(6.dp))

        TextButton(onClick = onForgotPassword, modifier = Modifier.align(Alignment.End)) {
            Text(text = stringResource(R.string.forgot_password), color = cs.primary)
        }

        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(textRes = R.string.sign_in, enabled = state.canSignIn, onClick = onSignIn)
        Spacer(modifier = Modifier.height(24.dp))
        DividerSection()
        Spacer(modifier = Modifier.height(24.dp))

        Button(
                onClick = onContinueGoogle,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = cs.secondaryContainer,
                                contentColor = cs.onSecondaryContainer
                        )
        ) {
            Text(text = stringResource(R.string.continue_with_google), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
                onClick = onRegister,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(18.dp),
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = cs.surfaceVariant,
                                contentColor = cs.onSurfaceVariant
                        )
        ) { Text(text = stringResource(R.string.register), fontWeight = FontWeight.Bold) }

        if (state.messageRes != null) {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surface),
                    modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(state.messageRes), color = cs.onSurface)
                    TextButton(onClick = onDismissMessage) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

/**
 * Contenido stateless de la pantalla de login. Recibe estado y callbacks sin depender de ViewModel
 * directamente.
 */
@Composable
fun LoginScreenContent(
        modifier: Modifier = Modifier,
        state: LoginUiState,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onForgotPassword: () -> Unit,
        onSignIn: () -> Unit,
        onContinueGoogle: () -> Unit,
        onRegister: () -> Unit,
        onDismissMessage: () -> Unit
) {
    LoginBackground {
        Column(
                modifier =
                        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            LoginHeader()
            Spacer(modifier = Modifier.height(5.dp))
            LoginForm(
                    state = state,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onForgotPassword = onForgotPassword,
                    onSignIn = onSignIn,
                    onContinueGoogle = onContinueGoogle,
                    onRegister = onRegister,
                    onDismissMessage = onDismissMessage
            )
        }
    }
}

@Preview(showBackground = true, name = "Login - Light")
@Composable
fun PreviewLoginLight() {
    CondorappTheme(darkTheme = false) { LoginScreenRoute(onSignInSuccess = {}) }
}

@Preview(showBackground = true, name = "Login - Dark")
@Composable
fun PreviewLoginDark() {
    CondorappTheme(darkTheme = true) { LoginScreenRoute(onSignInSuccess = {}) }
}
