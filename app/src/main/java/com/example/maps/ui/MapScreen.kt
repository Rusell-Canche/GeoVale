package com.example.maps.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(valeSeleccionado: String, onBack: () -> Unit) {
    val chetumal = LatLng(18.51957, -88.30397)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(chetumal, 13f)
    }

    val lugares = when (valeSeleccionado) {
        "Edenred" -> listOf(
            Lugar("Chedraui Plaza las Americas", 18.52030, -88.32093, "Supermercado"),
            Lugar("Soriana Chetumal", 18.52624, -88.29481, "Supermercado"),
            Lugar("Oxxo Andara", 18.53331, -88.27867, "Tienda de conveniencia")
        )
        "Sodexo" -> listOf(
            Lugar("Bodega Aurrera Chetumal", 18.5022, -88.3103, "Supermercado"),
            Lugar("Extra Insurgentes", 18.5045, -88.2960, "Supermercado")
        )
        "Sí Vale" -> listOf(
            Lugar("Super Willys Chetumal", 18.4990, -88.3035, "Supermercado"),
            Lugar("Farmacias del Ahorro", 18.5068, -88.2962, "Farmacia")
        )
        else -> listOf(
            Lugar("Comercial Mexicana Chetumal", 18.5011, -88.3078, "Supermercado"),
            Lugar("Oxxo Centro", 18.5032, -88.3057, "Tienda de conveniencia")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lugares válidos: $valeSeleccionado") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = cameraPositionState
        ) {
            lugares.forEach { lugar ->
                MarkerInfoWindowContent(
                    state = MarkerState(position = LatLng(lugar.lat, lugar.lng)),
                    title = lugar.nombre,
                    snippet = "Acepta $valeSeleccionado"
                ) { marker ->
                    InfoWindowContent(
                        lugar = lugar,
                        valeSeleccionado = valeSeleccionado
                    )
                }
            }
        }
    }
}

@Composable
fun InfoWindowContent(lugar: Lugar, valeSeleccionado: String) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Título del lugar
            Text(
                text = lugar.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tipo de establecimiento
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    text = "Tipo: ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = lugar.tipo,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Vale aceptado
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Acepta $valeSeleccionado",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Coordenadas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "Coordenadas",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lat: ${String.format("%.5f", lugar.lat)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Text(
                    text = "Lng: ${String.format("%.5f", lugar.lng)}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

data class Lugar(
    val nombre: String,
    val lat: Double,
    val lng: Double,
    val tipo: String = "Establecimiento"
)