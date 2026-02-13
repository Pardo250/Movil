package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R


@Composable
fun MensajeBienvendia(
    nombre: String,
    modifier: Modifier = Modifier
){
    Text("Descubre la magia de los ${nombre}",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = modifier
    )
}

@Composable
fun LogoApp(
    modifier: Modifier = Modifier
) {
        Image(
            painter = painterResource(R.drawable.logo_app),
            contentDescription = "Logo Condorapp",
            modifier = modifier
        )
    }

@Composable
fun AppButton(
    textoBoton: String = "Sign In",
    colorBoton: Color,
    modifier: Modifier = Modifier
){
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(
            containerColor = colorBoton
        ),
        modifier = modifier
    ) {
        Text(textoBoton)
    }
}

@Composable
fun BodyRegisterScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoApp()
        MensajeBienvendia("andes y más allá")
        AppButton("Sign In",
            colorBoton = Color(0xFF2B4025),
            modifier = Modifier.padding(top = 10.dp)
        )
        AppButton("Continua con Google",
            colorBoton = Color(0xFF4F7336),
            modifier = Modifier.padding(top = 10.dp)
        )
        AppButton("Registarse",
            colorBoton = Color(0xFFA6775B),
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BodyRegisterScreenPreview(){
    BodyRegisterScreen()
}

@Composable
fun RegisterScreen() {
    RegisterScreen()
    Box() {
        Image(
            painter = painterResource(R.drawable.fondo_pantalla),
            contentDescription = "Fondo Condorapp",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1F))
            BodyRegisterScreen()
            Spacer(modifier = Modifier.weight(1F))
            Text(
                "Condorapp all right reserved"
            )
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview(){
    RegisterScreen()
}

@Composable
fun FormularioRegistro(
    modifier: Modifier = Modifier
){
    TextField(
        value = "",
        onValueChange = {},
        label = { Text("Nombre") },
        modifier = modifier
    )
}




