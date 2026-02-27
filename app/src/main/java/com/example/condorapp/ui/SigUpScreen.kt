package com.example.condorapp.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.condorapp.R

val ScreenBg = Color(0xFFEDEFEA)
val PrimaryGreen = Color(0xFF2F4B2F)
val SoftGray = Color(0xFFE8E8E8)
val RegisterGreen = Color(0xFF557A3E)
val CancelBrown = Color(0xFFA87655)

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onCancelClick: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "condorapp",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryGreen
        )

        Text(
            text = "Descubre la magia de los\nAndes y más allá",
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))


        Text("Nombre y apellido", modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Nombre") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SoftGray,
                    focusedContainerColor = SoftGray
                )
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = { Text("Apellido") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SoftGray,
                    focusedContainerColor = SoftGray
                )
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text("Username", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(3.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Value") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SoftGray,
                focusedContainerColor = SoftGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Correo", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("name@example.com") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SoftGray,
                focusedContainerColor = SoftGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Contraseña", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Value") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SoftGray,
                focusedContainerColor = SoftGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Confirmar Contraseña", modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Value") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SoftGray,
                focusedContainerColor = SoftGray
            )
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RegisterGreen),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text("Registrarse", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CancelBrown),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text("Cancelar", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen(
        onRegisterClick = {},
        onCancelClick = {}
    )
}