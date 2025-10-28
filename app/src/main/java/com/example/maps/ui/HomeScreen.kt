package com.example.maps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onValeSelected: (String) -> Unit) {
    val vales = listOf(
        Vale("Edenred", Color(0xFFE53935), Icons.Default.CardGiftcard),
        Vale("Sodexo", Color(0xFF1E88E5), Icons.Default.Store),
        Vale("Sí Vale", Color(0xFF43A047), Icons.Default.LocalOffer),
        Vale("Up Sí Vale", Color(0xFFFDD835), Icons.Default.ShoppingBag)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Selecciona tu vale de despensa",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))

        vales.forEach { vale ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onValeSelected(vale.nombre) },
                colors = CardDefaults.cardColors(containerColor = vale.color),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        vale.nombre,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = vale.icon,
                        contentDescription = vale.nombre,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

data class Vale(val nombre: String, val color: Color, val icon: androidx.compose.ui.graphics.vector.ImageVector)
