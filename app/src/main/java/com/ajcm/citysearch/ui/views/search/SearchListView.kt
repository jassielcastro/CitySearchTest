package com.ajcm.citysearch.ui.views.search

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ajcm.citysearch.ui.views.SharedLocationViewModel
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.citysearch.ui.views.components.CityItemView
import com.ajcm.citysearch.ui.views.components.EmptyCitiesView
import com.ajcm.citysearch.ui.views.components.ErrorView
import com.ajcm.citysearch.ui.views.components.IdleView
import com.ajcm.citysearch.ui.views.components.LoadCityView
import com.ajcm.citysearch.ui.views.components.SearchBarView
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchListView(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
    onCitySelected: (Int) -> Unit
) {
    val sharedViewModel: SharedLocationViewModel = koinViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
    val citiesState by viewModel.citiesState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCities()
    }

    AnimatedContent(
        targetState = citiesState
    ) { state ->
        when (state) {
            is UiState.Loading -> {
                LoadCityView()
            }

            is UiState.Failure -> {
                ErrorView()
            }

            is UiState.Success -> {
                CitiesListView(
                    modifier = modifier,
                    viewModel = viewModel,
                    sharedViewModel = sharedViewModel,
                    onCitySelected = onCitySelected
                )
            }

            is UiState.Idle -> {
                IdleView()
            }
        }
    }
}

@Composable
fun CitiesListView(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    sharedViewModel: SharedLocationViewModel,
    onCitySelected: (Int) -> Unit
) {
    val cities = viewModel.cities.collectAsLazyPagingItems()
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        sharedViewModel.updateSelectedCity(null)
        viewModel.refreshTrigger
            .flowWithLifecycle(lifecycle.lifecycle)
            .collect {
                cities.refresh()
            }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        flingBehavior = ScrollableDefaults.flingBehavior(),
        state = rememberLazyListState(),
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                SearchBarView(
                    onFavoriteSelected = viewModel.isFavoriteSelected(),
                    onSearch = { prefix ->
                        viewModel.updateSearchQuery(prefix)
                    },
                    onFilterFavorites = { favorites ->
                        viewModel.updateSearchFavorites(favorites)
                    }
                )
            }
        }

        items(cities.itemCount) { index ->
            val city = cities[index]
            if (city != null) {
                CityItemView(
                    city = city,
                    onClick = {
                        onCitySelected(city.id)
                        sharedViewModel.updateSelectedCity(city)
                    },
                    onFavoriteClicked = {
                        viewModel.updateFavorite(city.id, !city.favorite)
                    }
                )
            }
        }

        if (cities.itemCount == 0 && cities.loadState.isIdle) {
            item {
                EmptyCitiesView()
            }
        }

        when (cities.loadState.append) {
            is LoadState.Loading -> item {
                CircularProgressIndicator(Modifier.padding(16.dp))
            }

            else -> Unit
        }

        item {
            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}
