package com.rdc.e09_logindefinitivo.screens.splash

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.rdc.e09_logindefinitivo.R
import com.rdc.e09_logindefinitivo.navigation.Screens
import com.rdc.e09_logindefinitivo.theme.FONDO
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    //Animación
    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.8f,
            animationSpec = keyframes {
                durationMillis = 2000
                0.0f at 0 using LinearEasing
                0.5f at 1000 using EaseInOutCubic
                0.9f at 1500 using EaseInOutCubic
                0.8f at 2000 using EaseInOutCubic
            }
        )



        delay(2000)

        navController.navigate(Screens.LoginScreen.name)  //poner esto al comienzo
        //si ya está logueado el usuario no necesita autenticarse de nuevo
        // poner esto al final
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(Screens.LoginScreen.name)
        } else {
            navController.navigate(Screens.HomeScreen.name) {
                // al pulsar botón atras vuelve a splash, para evitar esto sacamos
                // splash de la lista de pantallas recorridas
                popUpTo(Screens.SplashScreen.name) {
                    inclusive = true
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(250.dp)
            .scale(scale.value),
        shape = RectangleShape,
        border = BorderStroke(width = 2.dp, color = Color.LightGray),
        shadowElevation = 20.dp
    ) {
        Row(
            modifier = Modifier
                .background(FONDO)
                .padding(1.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.iconospotify),
                contentDescription = "Spotify Logo",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Spotify",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}