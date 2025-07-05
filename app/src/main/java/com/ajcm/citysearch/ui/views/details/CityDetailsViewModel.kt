package com.ajcm.citysearch.ui.views.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.ajcm.data_source_manager.repository.model.City
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityDetailsViewModel(
    private val repository: CitiesRepository
) : ViewModel() {

    private val _cityDetailsState: MutableStateFlow<UiState<City>> =
        MutableStateFlow(UiState.Idle)
    val cityDetailsState = _cityDetailsState.asStateFlow()

    fun loadCityDetails(cityId: Int) = viewModelScope.launch {
        _cityDetailsState.value = UiState.Loading

        repository.getCityById(cityId).takeIf { it != null }
            ?.let { city ->
                _cityDetailsState.value = UiState.Success(city)
            } ?: run {
            _cityDetailsState.value = UiState.Failure
        }
    }

    fun updateFavorite(cityId: Int, isFavorite: Boolean) = viewModelScope.launch {
        repository.updateFavorite(cityId, isFavorite)
    }
}
