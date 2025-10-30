package com.example.maps.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import kotlin.math.*
import androidx.compose.ui.platform.LocalContext
import com.example.maps.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(valeSeleccionado: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val chetumal = LatLng(18.51957, -88.30397)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(chetumal, 13f)
    }

    var selectedLugar by remember { mutableStateOf<Lugar?>(null) }
    var showFilters by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val lugares = when (valeSeleccionado) {
        "Edenred" -> listOf(
            Lugar("Chedraui Plaza las Americas", 18.52030, -88.32093, "Supermercado"),
            Lugar("Soriana Chetumal", 18.52624, -88.29481, "Supermercado"),
            Lugar("Oxxo Andara", 18.53331, -88.27867, "Tienda de conveniencia"),
            Lugar("Farmacia Similares", 18.52100, -88.31500, "Farmacia"),
            Lugar("Restaurante El Sabor", 18.51800, -88.30200, "Restaurante")
        )
        "Sodexo" -> listOf(
            Lugar("Bodega Aurrera Chetumal", 18.5022, -88.3103, "Supermercado"),
            Lugar("Extra Insurgentes", 18.5045, -88.2960, "Supermercado"),
            Lugar("Sam's Club", 18.5100, -88.3200, "Supermercado"),
            Lugar("Café Gourmet", 18.5055, -88.2980, "Restaurante")
        )
        "Sí Vale" -> listOf(
            Lugar("Super Willys Chetumal", 18.4990, -88.3035, "Supermercado"),
            Lugar("Farmacias del Ahorro", 18.5068, -88.2962, "Farmacia"),
            Lugar("Pizza Hut", 18.5020, -88.3000, "Restaurante"),
            Lugar("Oxxo Insurgentes", 18.5045, -88.2975, "Tienda de conveniencia")
        )
        else -> listOf(
            Lugar("Comercial Mexicana Chetumal", 18.5011, -88.3078, "Supermercado"),
            Lugar("Oxxo Centro", 18.5032, -88.3057, "Tienda de conveniencia")
        )
    }

    val filteredLugares = if (selectedFilter != null) {
        lugares.filter { it.tipo == selectedFilter }
    } else {
        lugares
    }

    val tipos = lugares.map { it.tipo }.distinct()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lugares válidos", fontSize = 16.sp)
                        Text(
                            valeSeleccionado,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = "Filtros",
                            tint = if (selectedFilter != null) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón para centrar en Chetumal
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(chetumal, 13f),
                                durationMs = 1000
                            )
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Default.MyLocation, "Centrar mapa")
                }

                // Contador de lugares
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "${filteredLugares.size} lugares",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Mapa
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = false,
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                        context,
                        R.raw.mapstyle_night
                    )
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = false,
                    myLocationButtonEnabled = false
                )
            ) {
                filteredLugares.forEach { lugar ->
                    val isSelected = selectedLugar == lugar
                    MarkerComposable(
                        state = MarkerState(position = LatLng(lugar.lat, lugar.lng)),
                        title = lugar.nombre,
                        snippet = lugar.tipo,
                        onClick = {
                            selectedLugar = lugar
                            scope.launch {
                                val index = filteredLugares.indexOf(lugar)
                                if (index != -1) {
                                    listState.animateScrollToItem(index)
                                }
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(lugar.lat, lugar.lng),
                                        15f
                                    ),
                                    durationMs = 800
                                )
                            }
                            true
                        }
                    ) {
                        CustomMarker(
                            tipo = lugar.tipo,
                            isSelected = isSelected
                        )
                    }
                }
            }

            // Filtros
            AnimatedVisibility(
                visible = showFilters,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    tonalElevation = 4.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedFilter == null,
                            onClick = { selectedFilter = null },
                            label = { Text("Todos") }
                        )
                        tipos.forEach { tipo ->
                            FilterChip(
                                selected = selectedFilter == tipo,
                                onClick = {
                                    selectedFilter = if (selectedFilter == tipo) null else tipo
                                },
                                label = { Text(tipo) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getIconForTipo(tipo),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // Lista de lugares en la parte inferior
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(180.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shadowElevation = 16.dp,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column {
                    // Handle indicator
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                            .align(Alignment.CenterHorizontally)
                    )

                    LazyRow(
                        state = listState,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredLugares) { lugar ->
                            LugarCard(
                                lugar = lugar,
                                valeSeleccionado = valeSeleccionado,
                                isSelected = selectedLugar == lugar,
                                userLocation = chetumal,
                                onClick = {
                                    selectedLugar = lugar
                                    scope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(lugar.lat, lugar.lng),
                                                15f
                                            ),
                                            durationMs = 800
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomMarker(tipo: String, isSelected: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.3f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val color = getColorForTipo(tipo)

    Box(
        modifier = Modifier.scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // Sombra/resplandor si está seleccionado
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(color.copy(alpha = 0.3f), CircleShape)
            )
        }

        // Marcador principal
        Surface(
            shape = CircleShape,
            color = color,
            shadowElevation = if (isSelected) 8.dp else 4.dp,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = getIconForTipo(tipo),
                contentDescription = tipo,
                tint = Color.White,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LugarCard(
    lugar: Lugar,
    valeSeleccionado: String,
    isSelected: Boolean,
    userLocation: LatLng,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    val distance = calculateDistance(
        userLocation.latitude, userLocation.longitude,
        lugar.lat, lugar.lng
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icono
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = getColorForTipo(lugar.tipo),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = getIconForTipo(lugar.tipo),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

            // Información
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = lugar.nombre,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = String.format("%.1f km", distance),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = lugar.tipo,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Botón de navegación
            IconButton(
                onClick = { /* Abrir Google Maps */ },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Directions,
                    contentDescription = "Navegar",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

fun getColorForTipo(tipo: String): Color {
    return when (tipo) {
        "Supermercado" -> Color(0xFF4CAF50)
        "Tienda de conveniencia" -> Color(0xFF2196F3)
        "Farmacia" -> Color(0xFFE91E63)
        "Restaurante" -> Color(0xFFFF9800)
        else -> Color(0xFF9C27B0)
    }
}

fun getIconForTipo(tipo: String): ImageVector {
    return when (tipo) {
        "Supermercado" -> Icons.Default.ShoppingCart
        "Tienda de conveniencia" -> Icons.Default.Store
        "Farmacia" -> Icons.Default.LocalHospital
        "Restaurante" -> Icons.Default.Restaurant
        else -> Icons.Default.Place
    }
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371 // Radio de la Tierra en km
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c
}

data class Lugar(
    val nombre: String,
    val lat: Double,
    val lng: Double,
    val tipo: String = "Establecimiento"
)