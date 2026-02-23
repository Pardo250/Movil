package com.example.condorapp.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun LoginBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
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
            modifier = Modifier.size(260.dp)
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmailField(modifier: Modifier = Modifier, email: String, onEmailChange: (String) -> Unit) {
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
fun PasswordField(modifier: Modifier = Modifier, password: String, onPasswordChange: (String) -> Unit) {
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
fun PrimaryButton(modifier: Modifier = Modifier, textRes: Int, onClick: () -> Unit, enabled: Boolean = true) {
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
        Text(stringResource(R.string.or), modifier = Modifier.padding(horizontal = 16.dp))
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
    Column(modifier = modifier) {
        EmailField(email = state.email, onEmailChange = onEmailChange)
        Spacer(modifier = Modifier.height(24.dp))
        PasswordField(password = state.password, onPasswordChange = onPasswordChange)
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onForgotPassword, modifier = Modifier.align(Alignment.End)) {
            Text(text = stringResource(R.string.forgot_password))
        }
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(textRes = R.string.sign_in, enabled = state.canSignIn, onClick = onSignIn)
        Spacer(modifier = Modifier.height(24.dp))
        DividerSection()
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onContinueGoogle,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = stringResource(R.string.continue_with_google), fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRegister,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f))
        ) {
            Text(text = stringResource(R.string.register), fontWeight = FontWeight.Bold)
        }

        if (state.messageRes != null) {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(state.messageRes))
                    TextButton(onClick = onDismissMessage) { Text("OK") }
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
                state = state.copy(messageRes = R.string.sign_in)
                onSignInSuccess()
            }
        },
        onContinueGoogle = { state = state.copy(messageRes = R.string.continue_with_google) },
        onRegister = {
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
fun LoginScreenPreview() {
    CondorappTheme {
        LoginScreenRoute()
    }
}
