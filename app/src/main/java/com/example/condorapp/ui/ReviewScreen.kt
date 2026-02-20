package com.example.condorapp.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ScreenBg = Color(0xFFEDEFEA)
val PrimaryGreen = Color(0xFF2F4B2F)
val SoftGreen2 = Color(0xFF8FA77B)
val StarBeige = Color(0xFFC7B1A1)
val StarInactive = Color(0xFFD9D9D9)
val ButtonGreen = Color(0xFF4F7336)

@Composable
fun ReviewExperienceScreen(
    onBackClick: () -> Unit
) {

    var rating by remember { mutableStateOf(4) }
    var comment by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Flecha
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(SoftGreen2)
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "¿Que tal estuvo tu aventura?",
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen,
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡Bienvenido de vuelta! Para nosotros, nada es más valioso que tu perspectiva. Al compartir tu experiencia, nos ayudas a construir la guía de viajes más auténtica.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "TU CALIFICACION",
                color = PrimaryGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ⭐ ESTRELLAS EN SEMICÍRCULO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val offsets = listOf(20.dp, 10.dp, 0.dp, 10.dp, 20.dp)

                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) StarBeige else StarInactive,
                            modifier = Modifier
                                .size(48.dp)
                                .offset(y = offsets[index])
                                .clickable { rating = index + 1 }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Añadir imagen",
                fontWeight = FontWeight.Medium,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF4F4F4))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = SoftGreen2,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Comentario", fontSize = 18.sp)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(120.dp)) // espacio para botón
        }

        Button(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Text(
                text = "Publicar Reseña",
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewReviewExperience() {
    ReviewExperienceScreen(onBackClick = {})
}