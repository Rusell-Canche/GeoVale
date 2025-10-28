package com.example.maps

import androidx.compose.runtime.*
import com.example.maps.ui.HomeScreen
import com.example.maps.ui.MapScreen

@Composable
fun AppNavigation() {
    var selectedVale by remember { mutableStateOf<String?>(null) }

    if (selectedVale == null) {
        HomeScreen(onValeSelected = { vale ->
            selectedVale = vale
        })
    } else {
        MapScreen(
            valeSeleccionado = selectedVale!!,
            onBack = { selectedVale = null }
        )
    }
}
