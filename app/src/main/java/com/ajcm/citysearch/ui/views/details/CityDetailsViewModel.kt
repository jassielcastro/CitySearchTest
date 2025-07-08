package com.ajcm.citysearch.ui.views.details

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ajcm.citysearch.BuildConfig
import com.ajcm.citysearch.ui.mapper.toCity
import com.ajcm.citysearch.ui.mapper.toCityWater
import com.ajcm.citysearch.ui.model.City
import com.ajcm.citysearch.ui.model.CityWater
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.ajcm.data_source_manager.repository.CityWeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityDetailsViewModel(
    private val repository: CitiesRepository,
    private val waterRepository: CityWeatherRepository
) : ViewModel() {

    var selectedCity = MutableStateFlow<City?>(null)
        private set

    @VisibleForTesting(otherwise = PRIVATE)
    val mCityDetailsState: MutableStateFlow<UiState<CityWater>> =
        MutableStateFlow(UiState.Idle)
    val cityDetailsState = mCityDetailsState.asStateFlow()

    fun loadCity(cityId: Int) = viewModelScope.launch {
        if (selectedCity.value != null) {
            return@launch
        }
        val city = repository.getCityById(cityId).takeIf { it != null }?.toCity() ?: run {
            return@launch
        }

        selectedCity.value = city
    }

    fun loadCityDetails(coordinates: Pair<Double, Double>) = viewModelScope.launch {
        if (mCityDetailsState.value !is UiState.Idle) {
            return@launch
        }
        mCityDetailsState.value = UiState.Loading

        waterRepository.getWaterBy(
            latitude = coordinates.first,
            longitude = coordinates.second,
            apiKey = BuildConfig.WEATHER_API_KEY
        ).onSuccess { result ->
            mCityDetailsState.value = UiState.Success(result.toCityWater())
        }.onFailure {
            it.printStackTrace()
            mCityDetailsState.value = UiState.Failure
        }
    }

    fun updateFavorite(cityId: Int, isFavorite: Boolean) = viewModelScope.launch {
        repository.updateFavorite(cityId, isFavorite)
    }
}
