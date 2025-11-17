package com.example.maps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maps.data.model.Establecimiento
import com.example.maps.data.repository.EstablecimientoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstablecimientoViewModel(
    private val repository: EstablecimientoRepository = EstablecimientoRepository()
) : ViewModel() {

    private val _establecimientos = MutableStateFlow<List<Establecimiento>>(emptyList())
    val establecimientos: StateFlow<List<Establecimiento>> = _establecimientos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadEstablecimientos(proveedorId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _establecimientos.value = repository.getEstablecimientos(proveedorId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
