package com.example.maps

import androidx.compose.runtime.*
import com.example.maps.ui.HomeScreen
import com.example.maps.ui.MapScreen

@Composable
fun AppNavigation() {
    var proveedorSeleccionado  by remember { mutableStateOf<ProveedorSeleccionado?>(null) }

    if (proveedorSeleccionado  == null) {
        HomeScreen(
            onProveedorSeleccionado = { proveedor ->
            proveedorSeleccionado  = proveedor
        })
    } else {
        MapScreen(
            valeID = proveedorSeleccionado!!.id,
            valeNombre = proveedorSeleccionado!!.nombre,
            onBack = { proveedorSeleccionado = null }
        )
    }
}

data class ProveedorSeleccionado(
    val id: String,
    val nombre: String
)
