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

@Preview(showBackground = true)
@Composable
fun LoginBackgroundPreview() {
    LoginBackground { }
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

@Preview(showBackground = true)
@Composable
fun LoginHeaderPreview() {
    LoginHeader()
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

@Preview(showBackground = true)
@Composable
fun EmailFieldPreview() {
    EmailField(email = "", onEmailChange = {})
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

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview() {
    PasswordField(password = "", onPasswordChange = {})
}

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    textRes: Int,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
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

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton(
        textRes = R.string.sign_in,
        color = CondorColors.DarkGreen
    ) {}
}

@Composable
fun DividerSection(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text(stringResource(R.string.or), modifier = Modifier.padding(horizontal = 16.dp))
        Divider(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun DividerSectionPreview() {
    DividerSection()
}

@Composable
fun LoginForm(
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier) {

        EmailField(email = email, onEmailChange = { email = it })

        Spacer(modifier = Modifier.height(24.dp))

        PasswordField(password = password, onPasswordChange = { password = it })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            textRes = R.string.sign_in,
            color = CondorColors.DarkGreen
        ) {}

        Spacer(modifier = Modifier.height(24.dp))

        DividerSection()

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            textRes = R.string.continue_with_google,
            color = CondorColors.Green
        ) {}

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            textRes = R.string.register,
            color = CondorColors.Brown
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    LoginForm()
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
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
            LoginForm()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
