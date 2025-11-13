package com.example.maps.data.repository

import com.example.maps.data.remote.RetrofitClient
import com.example.maps.data.model.Proveedor

class ProveedorValeRepository {
    suspend fun getProveedores(): List<Proveedor> {
        return RetrofitClient.apiService.getProveedoresVales().proveedores
    }
}
