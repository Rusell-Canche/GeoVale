package com.example.maps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maps.data.model.Proveedor
import com.example.maps.data.repository.ProveedorValeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProveedorViewModel(
    private val repository: ProveedorValeRepository = ProveedorValeRepository()
) : ViewModel() {

    private val _proveedores = MutableStateFlow<List<Proveedor>>(emptyList())
    val proveedores: StateFlow<List<Proveedor>> = _proveedores

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadProveedores() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _proveedores.value = repository.getProveedores()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
