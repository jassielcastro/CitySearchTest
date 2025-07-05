package com.ajcm.citysearch.ui.views.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.ajcm.data_source_manager.repository.model.City
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: CitiesRepository
) : ViewModel() {

    private val _citiesState: MutableStateFlow<UiState<Unit>> =
        MutableStateFlow(UiState.Idle)
    val citiesState = _citiesState.asStateFlow()

    private val _favorite = MutableStateFlow(0)
    private val _prefix = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    private val debouncedPrefix = _prefix
        .debounce(150)
        .distinctUntilChanged()

    private val _refreshTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val refreshTrigger = _refreshTrigger.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val cities: Flow<PagingData<City>> = combine(
        _favorite, debouncedPrefix
    ) { favorite, prefix ->
        favorite to prefix
    }.debounce(100)
        .flatMapLatest { (favorite, prefix) ->
            repository.getCitiesBy(favorite, prefix).cachedIn(viewModelScope)
        }

    fun loadCities() = viewModelScope.launch {
        _citiesState.value = UiState.Loading

        if (!repository.areCitiesPopulated()) {
            repository.fetchCitiesFromRemote().onFailure {
                _citiesState.value = UiState.Failure
                return@launch
            }
        }

        _citiesState.value = UiState.Success(Unit)
    }

    fun updateSearchQuery(query: String) {
        _prefix.value = query
    }

    fun updateSearchFavorites(favorites: Boolean) {
        _favorite.value = if (favorites) 1 else 0
    }

    fun updateFavorite(cityId: Int, isFavorite: Boolean) = viewModelScope.launch {
        repository.updateFavorite(cityId, isFavorite)
        _refreshTrigger.emit(Unit)
    }
}
