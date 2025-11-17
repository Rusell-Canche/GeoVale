package com.example.maps.data.repository

import com.example.maps.data.remote.RetrofitClient
import com.example.maps.data.model.Establecimiento

class EstablecimientoRepository {
    private val api = RetrofitClient.apiService

    suspend fun getEstablecimientos(proveedorId: String): List<Establecimiento> {
        return api.getEstablecimientos(proveedorId).data
    }
}
