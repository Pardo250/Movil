package com.example.condorapp.ui

import android.util.Log
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
import com.example.condorapp.R
import com.example.condorapp.ui.theme.CondorappTheme

private const val TAG = "LoginScreen"

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val messageRes: Int? = null
) {
    val canSignIn: Boolean
        get() = email.isNotBlank() && password.isNotBlank()
}

@Composable
fun LoginBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo2),
            contentDescription = stringResource(R.string.cd_logo_condorapp),
            modifier = Modifier.size(210.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmailField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.email_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.email_placeholder)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.password_label),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.value_placeholder)) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

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
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text = stringResource(textRes), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DividerSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
        Text(
            text = stringResource(R.string.or),
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
    }
}

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

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onForgotPassword, modifier = Modifier.align(Alignment.End)) {
            Text(text = stringResource(R.string.forgot_password), color = cs.primary)
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            textRes = R.string.sign_in,
            enabled = state.canSignIn,
            onClick = onSignIn
        )

        Spacer(modifier = Modifier.height(24.dp))

        DividerSection()

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Botón Google: usar secondaryContainer para que se vea bien en dark
        Button(
            onClick = onContinueGoogle,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.secondaryContainer,
                contentColor = cs.onSecondaryContainer
            )
        ) {
            Text(text = stringResource(R.string.continue_with_google), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRegister,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = cs.surfaceVariant,
                contentColor = cs.onSurfaceVariant
            )
        ) {
            Text(text = stringResource(R.string.register), fontWeight = FontWeight.Bold)
        }

        // ✅ Mensaje
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
                    Text(
                        text = stringResource(state.messageRes),
                        color = cs.onSurface
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
fun LoginScreenRoute(
    modifier: Modifier = Modifier,
    onSignInSuccess: () -> Unit = {},
    onGoRegister: () -> Unit = {}
) {
    var state by remember { mutableStateOf(LoginUiState()) }

    LoginScreenContent(
        modifier = modifier,
        state = state,
        onEmailChange = { state = state.copy(email = it, messageRes = null) },
        onPasswordChange = { state = state.copy(password = it, messageRes = null) },
        onForgotPassword = { state = state.copy(messageRes = R.string.forgot_password) },
        onSignIn = {
            if (state.canSignIn) {
                println("Inicio de sesión -> Email: ${state.email}, Password: ${state.password}")
                state = state.copy(messageRes = R.string.sign_in)
                onSignInSuccess()
            }
        },
        onContinueGoogle = { state = state.copy(messageRes = R.string.continue_with_google) },
        onRegister = {
            println("Registro iniciado -> Email: ${state.email}, Password: ${state.password}")
            Log.d(TAG, "Registro iniciado -> Email: ${state.email}, Password: ${state.password}")
            state = state.copy(messageRes = R.string.register)
            onGoRegister()
        },
        onDismissMessage = { state = state.copy(messageRes = null) }
    )
}

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
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenLightPreview() {
    CondorappTheme(darkTheme = false) {
        LoginScreenRoute()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenDarkPreview() {
    CondorappTheme(darkTheme = true) {
        LoginScreenRoute()
    }
}