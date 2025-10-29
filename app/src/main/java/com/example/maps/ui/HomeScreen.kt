package com.example.maps.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onValeSelected: (String) -> Unit) {

    val vales = listOf(
        Vale("Edenred", Color(0xFFD32F2F), Icons.Default.CardGiftcard),
        Vale("Sodexo", Color(0xFF1976D2), Icons.Default.Store),
        Vale("Sí Vale", Color(0xFF388E3C), Icons.Default.LocalOffer),
        Vale("Up Sí Vale", Color(0xFFFBC02D), Icons.Default.ShoppingBag)
    )

    Scaffold(
        containerColor = Color(0xFFF5F5F5)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // ✅ Título estilo Google Wallet
            Text(
                "Mis vales",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Selecciona un vale para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            // ✅ Tarjetas tipo Google Pay / Wallet
            vales.forEach { vale ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .clickable { onValeSelected(vale.nombre) }
                        .animateContentSize(),
                    shape = RoundedCornerShape(18.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        // ✅ Ícono en Google Pay style
                        Box(
                            modifier = Modifier
                                .size(55.dp)
                                .background(
                                    color = vale.color.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = vale.icon,
                                contentDescription = null,
                                tint = vale.color,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // ✅ Info del vale estilo pase digital
                        Column {
                            Text(
                                vale.nombre,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Vale electrónico",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

data class Vale(
    val nombre: String,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
