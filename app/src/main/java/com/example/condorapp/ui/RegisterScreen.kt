package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
fun LoginBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.Olivafeed))
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun LoginBackgroundPreview() {
    LoginBackground {}
}

@Composable
fun LoginHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(R.drawable.logo2),
            contentDescription = "Logo Condorapp",
            modifier = Modifier.size(260.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Descubre la magia de los\nAndes y más allá",
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
fun EmailField(email: String, onEmailChange: (String) -> Unit) {

    Column {
        Text(
            text = "Email",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = CondorColors.DarkGreen
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("name@example.com")
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmailFieldPreview() {
    EmailField("", {})
}

@Composable
fun PasswordField(password: String, onPasswordChange: (String) -> Unit) {

    Column {
        Text(
            text = "Password",
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
            placeholder = { Text("Value") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview() {
    PasswordField("", {})
}

@Composable
fun PrimaryButton(
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrimaryButtonPreview() {
    PrimaryButton("Sign In", CondorColors.DarkGreen) {}
}

@Composable
fun DividerSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(modifier = Modifier.weight(1f))
        Text("o", modifier = Modifier.padding(horizontal = 16.dp))
        Divider(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun DividerSectionPreview() {
    DividerSection()
}

@Composable
fun LoginForm() {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column {

        EmailField(email) { email = it }

        Spacer(modifier = Modifier.height(24.dp))

        PasswordField(password) { password = it }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forgot password?",
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton("Sign In", CondorColors.DarkGreen) {}

        Spacer(modifier = Modifier.height(24.dp))

        DividerSection()

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton("Continua con Google", CondorColors.Green) {}

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton("Registrarse", CondorColors.Brown) {}
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    LoginForm()
}

@Composable
fun LoginScreen() {
    LoginBackground {
        Column(
            modifier = Modifier
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

