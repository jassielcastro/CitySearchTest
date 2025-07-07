package com.ajcm.citysearch.ui.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajcm.citysearch.ui.model.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SharedLocationViewModel : ViewModel() {
    var selectedCity = MutableStateFlow<City?>(null)
        private set

    fun updateSelectedCity(city: City?) = viewModelScope.launch {
        selectedCity.value = city
    }
}
