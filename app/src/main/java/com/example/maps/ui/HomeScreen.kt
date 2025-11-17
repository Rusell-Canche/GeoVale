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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.maps.ProveedorSeleccionado
import com.example.maps.ui.viewmodel.ProveedorViewModel
@Composable
fun HomeScreen(onProveedorSeleccionado: (ProveedorSeleccionado) -> Unit) {
    val viewModel: ProveedorViewModel = viewModel()
    val proveedores by viewModel.proveedores.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadProveedores()
    }

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
            when {
                isLoading -> CircularProgressIndicator()
                proveedores.isEmpty() -> Text("No hay proveedores disponibles.")
                else -> proveedores.forEach { proveedor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                            .clickable { onProveedorSeleccionado( ProveedorSeleccionado(
                                id = proveedor.id.toString(),
                                nombre = proveedor.nombre)) }
                            .animateContentSize(),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(3.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(proveedor.nombre, style = MaterialTheme.typography.titleLarge)
                                Text("Vale electrónico", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ProveedorSeleccionado(
    val id: String,
    val nombre: String
)