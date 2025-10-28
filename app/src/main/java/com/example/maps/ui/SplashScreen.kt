package com.example.maps.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun StormSplashScreen(onTimeout: () -> Unit) {
    var raindrops by remember { mutableStateOf(generateRainDrops(80)) }
    var lightningVisible by remember { mutableStateOf(false) }

    // Animación de caída de lluvia
    LaunchedEffect(Unit) {
        while (true) {
            raindrops = raindrops.map { drop ->
                val newY = drop.y + drop.speed
                if (newY > 2000f)
                    drop.copy(y = Random.nextFloat() * -100f, x = Random.nextFloat() * 1080f)
                else
                    drop.copy(y = newY)
            }
            delay(16) // ~60 FPS
        }
    }

    // Relámpagos aleatorios
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(1000, 4000))
            lightningVisible = true
            delay(150)
            lightningVisible = false
        }
    }

    // Timeout para pasar a la siguiente pantalla
    LaunchedEffect(Unit) {
        delay(4000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (lightningVisible) Color(0xFFEEEEEE) else Color(0xFF001020)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            raindrops.forEach { drop ->
                drawLine(
                    color = Color(0xFF55AFFF),
                    start = Offset(drop.x, drop.y),
                    end = Offset(drop.x, drop.y + drop.length),
                    strokeWidth = 6f // gotas más gruesas
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Storm Tracker",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Cargando...",
                color = Color(0xFF99CCFF),
                fontSize = 18.sp
            )
        }
    }
}

data class RainDrop(val x: Float, val y: Float, val length: Float, val speed: Float)

fun generateRainDrops(count: Int): List<RainDrop> {
    return List(count) {
        RainDrop(
            x = Random.nextFloat() * 1080f,
            y = Random.nextFloat() * 2000f,
            length = Random.nextFloat() * 40f + 40f,
            speed = Random.nextFloat() * 25f + 15f
        )
    }
}
