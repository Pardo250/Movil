package com.example.condorapp.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R

object CondorColors {
    val DarkGreen = Color(0xFF2C4A3E)
    val Green = Color(0xFF4F7942)
    val Brown = Color(0xFFB08968)
    val LightBackground = Color(0xFFF8F8F8)
    val Gray = Color(0xFF858585)
    val White = Color(0xFFFFFFFF)
    val DividerGray = Color(0xFFD0D0D0)
}

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
            .background(colorResource(id = R.color.Olivafeed))
    ) {
        content()
    }
}

@Composable
fun LoginHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo2),
            contentDescription = stringResource(R.string.cd_logo_condorapp),
            modifier = Modifier.size(260.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = 20.sp,
            color = CondorColors.Gray,
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
            color = CondorColors.DarkGreen
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
            color = CondorColors.DarkGreen
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
    color: Color,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = stringResource(textRes),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DividerSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(stringResource(R.string.or), modifier = Modifier.padding(horizontal = 16.dp))
        Divider(modifier = Modifier.weight(1f))
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
    Column(modifier = modifier) {

        EmailField(email = state.email, onEmailChange = onEmailChange)

        Spacer(modifier = Modifier.height(24.dp))

        PasswordField(password = state.password, onPasswordChange = onPasswordChange)

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(R.string.forgot_password))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ Interacción: se habilita solo si hay datos
        PrimaryButton(
            textRes = R.string.sign_in,
            color = CondorColors.DarkGreen,
            enabled = state.canSignIn,
            onClick = onSignIn
        )

        Spacer(modifier = Modifier.height(24.dp))

        DividerSection()

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            textRes = R.string.continue_with_google,
            color = CondorColors.Green,
            onClick = onContinueGoogle
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            textRes = R.string.register,
            color = CondorColors.Brown,
            onClick = onRegister
        )

        // ✅ Feedback visible (interacción)
        if (state.messageRes != null) {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(state.messageRes))
                    TextButton(onClick = onDismissMessage) {
                        Text("OK")
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
        onForgotPassword = {
            // ejemplo de interacción
            state = state.copy(messageRes = R.string.forgot_password)
        },
        onSignIn = {
            // Validación simple (puedes cambiarla)
            if (state.canSignIn) {
                state = state.copy(messageRes = R.string.sign_in)
                onSignInSuccess()
            }
        },
        onContinueGoogle = {
            state = state.copy(messageRes = R.string.continue_with_google)
        },
        onRegister = {
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
fun LoginScreenPreview() {
    LoginScreenRoute()
}
