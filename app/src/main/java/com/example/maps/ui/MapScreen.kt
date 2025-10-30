package com.example.maps.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
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
    var showProductos by remember { mutableStateOf(false) }
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

    // Modal de productos canjeables
    if (showProductos) {
        ProductosCanjeablesModal(
            valeSeleccionado = valeSeleccionado,
            onDismiss = { showProductos = false }
        )
    }

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
                    IconButton(onClick = { showProductos = true }) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                Icons.Default.ShoppingBag,
                                contentDescription = "Productos canjeables"
                            )
                        }
                    }
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
                                },
                                onNavigate = {
                                    val uri = Uri.parse("google.navigation:q=${lugar.lat},${lugar.lng}")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    intent.setPackage("com.google.android.apps.maps")

                                    if (intent.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(intent)
                                    } else {
                                        // Si no tiene Google Maps, usar el navegador
                                        val browserUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${lugar.lat},${lugar.lng}")
                                        context.startActivity(Intent(Intent.ACTION_VIEW, browserUri))
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
        ), label = ""
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
    onClick: () -> Unit,
    onNavigate: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
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
            FilledIconButton(
                onClick = onNavigate,
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Directions,
                    contentDescription = "Cómo llegar",
                    tint = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosCanjeablesModal(
    valeSeleccionado: String,
    onDismiss: () -> Unit
) {
    val productos = getProductosPorVale(valeSeleccionado)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Productos Canjeables",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "con $valeSeleccionado",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.padding(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de productos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(productos) { producto ->
                    ProductoItem(producto)
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = producto.color,
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = producto.icono,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(14.dp)
                )
            }

            // Información del producto
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = producto.categoria,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = producto.color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = producto.restriccion,
                        fontSize = 11.sp,
                        color = producto.color,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Disponibilidad
            Surface(
                shape = CircleShape,
                color = if (producto.disponible)
                    Color(0xFF4CAF50).copy(alpha = 0.2f)
                else
                    Color(0xFFFF5252).copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = if (producto.disponible) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = if (producto.disponible) Color(0xFF4CAF50) else Color(0xFFFF5252),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

fun getProductosPorVale(vale: String): List<Producto> {
    return when (vale) {
        "Edenred" -> listOf(
            Producto("Despensa completa", "Alimentos básicos", "Sin restricciones", Icons.Default.ShoppingCart, Color(0xFF4CAF50), true),
            Producto("Frutas y verduras", "Productos frescos", "Vigencia 30 días", Icons.Default.LocalFlorist, Color(0xFF8BC34A), true),
            Producto("Lácteos", "Leche, queso, yogurt", "Refrigerados", Icons.Default.Icecream, Color(0xFF03A9F4), true),
            Producto("Carnes y embutidos", "Proteínas", "Vigencia 15 días", Icons.Default.Restaurant, Color(0xFFFF5722), true),
            Producto("Pan y tortillas", "Panificados", "Consumo diario", Icons.Default.Fastfood, Color(0xFFFF9800), true),
            Producto("Bebidas", "Agua, jugos, refrescos", "No alcohólicas", Icons.Default.LocalDrink, Color(0xFF2196F3), true),
            Producto("Productos de limpieza", "Hogar", "Uso doméstico", Icons.Default.CleaningServices, Color(0xFF9C27B0), false)
        )
        "Sodexo" -> listOf(
            Producto("Alimentos preparados", "Comida lista", "Restaurantes afiliados", Icons.Default.Restaurant, Color(0xFFFF9800), true),
            Producto("Menú del día", "Comida corrida", "Lun-Vie", Icons.Default.Fastfood, Color(0xFFFF5722), true),
            Producto("Desayunos", "Comida matutina", "Antes de 12pm", Icons.Default.FreeBreakfast, Color(0xFFFFC107), true),
            Producto("Café y postres", "Cafeterías", "Todo el día", Icons.Default.Cake, Color(0xFF795548), true),
            Producto("Despensa básica", "Supermercados", "Productos limitados", Icons.Default.ShoppingCart, Color(0xFF4CAF50), true),
            Producto("Comida rápida", "Fast food", "Cadenas autorizadas", Icons.Default.LocalPizza, Color(0xFFE91E63), false)
        )
        "Sí Vale" -> listOf(
            Producto("Víveres generales", "Supermercado", "Amplio catálogo", Icons.Default.ShoppingCart, Color(0xFF4CAF50), true),
            Producto("Medicamentos", "Farmacias", "Con receta", Icons.Default.LocalHospital, Color(0xFFE91E63), true),
            Producto("Productos de higiene", "Cuidado personal", "Aseo personal", Icons.Default.Spa, Color(0xFF9C27B0), true),
            Producto("Alimentos perecederos", "Frescos", "Consumo inmediato", Icons.Default.LocalFlorist, Color(0xFF8BC34A), true),
            Producto("Bebidas sin alcohol", "Líquidos", "No alcohólicas", Icons.Default.LocalDrink, Color(0xFF2196F3), true),
            Producto("Tiendas de conveniencia", "Oxxo, 7-Eleven", "Productos limitados", Icons.Default.Store, Color(0xFF00BCD4), true),
            Producto("Gasolina", "Combustible", "Estaciones autorizadas", Icons.Default.LocalGasStation, Color(0xFFFF5722), false)
        )
        else -> listOf(
            Producto("Consulta disponibilidad", "Información", "Contacta a tu proveedor", Icons.Default.Info, Color(0xFF607D8B), false)
        )
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

data class Producto(
    val nombre: String,
    val categoria: String,
    val restriccion: String,
    val icono: ImageVector,
    val color: Color,
    val disponible: Boolean
)