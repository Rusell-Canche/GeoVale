package com.example.maps.data.model

data class Proveedor(
    val id: String,
    val nombre: String,
    val descripcion: String?,
    val sitio_web: String?,
    val logo: String?,
    val activo: Boolean
)
