package com.example.maps.data.remote

import android.os.Message
import com.example.maps.data.model.Establecimiento
import com.example.maps.data.model.Proveedor
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("proveedores")
    suspend fun getProveedoresVales(): ApiResponse<List<Proveedor>>

    @GET("proveedores/{id}/establecimientos")
    suspend fun getEstablecimientos(
        @Path("id") proveedorId: String
    ): ApiResponse<List<Establecimiento>>
}

/**
 * Envuelve el formato JSON que devuelve Laravel:
 * {
 *   "data": [ { ... } ]
 * }
 */
data class ApiResponse<T>(
    val data: T,
    val message: String,
    val success: Boolean
)
