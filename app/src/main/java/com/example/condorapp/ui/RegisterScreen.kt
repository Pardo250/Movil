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
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CondorColors.LightBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo
            Image(
                painter = painterResource(R.drawable.logo_app),
                contentDescription = "Logo Condorapp",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Descubre la magia de los\nAndes y más allá",
                fontSize = 16.sp,
                color = CondorColors.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            LoginForm()

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun LoginForm() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Email",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = CondorColors.DarkGreen,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "name@example.com",
                    color = CondorColors.Gray.copy(alpha = 0.6f)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = CondorColors.Green,
                focusedBorderColor = CondorColors.Green,
                unfocusedContainerColor = CondorColors.White,
                focusedContainerColor = CondorColors.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Password",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = CondorColors.DarkGreen,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Value",
                    color = CondorColors.Gray.copy(alpha = 0.6f)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = CondorColors.Green,
                focusedBorderColor = CondorColors.Green,
                unfocusedContainerColor = CondorColors.White,
                focusedContainerColor = CondorColors.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forgot password?",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = CondorColors.DarkGreen,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { /* TODO: Navigate to forgot password */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO: Handle sign in */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CondorColors.DarkGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Sign In",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = CondorColors.DividerGray,
                thickness = 1.dp
            )
            Text(
                text = "o",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CondorColors.Gray,
                fontSize = 14.sp
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = CondorColors.DividerGray,
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* TODO: Handle Google sign in */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CondorColors.Green
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Continua con Google",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Navigate to register */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CondorColors.Brown
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Registrarse",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}