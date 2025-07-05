package com.ajcm.citysearch.ui.views.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.ajcm.citysearch.R
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.citysearch.ui.views.components.CityItemView
import com.ajcm.citysearch.ui.views.components.SearchBarView
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchListView(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {
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
) {
    val cities = viewModel.cities.collectAsLazyPagingItems()
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
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
                    onClick = { /* Handle city click */ },
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

@Composable
fun EmptyCitiesView(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = Icons.Default.Place,
            contentDescription = "No match found Icon",
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(
            modifier = Modifier
                .size(14.dp)
        )

        Text(
            text = stringResource(R.string.search_empty_message),
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
fun LoadCityView(
    modifier: Modifier = Modifier,
    loadingText: List<Int> = listOf(
        R.string.loading_hint_1,
        R.string.loading_hint_2,
        R.string.loading_hint_3,
        R.string.loading_hint_4,
    ),
    displayTime: Long = 2_500L
) {
    var currentItemIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(currentItemIndex) {
        delay(displayTime)
        currentItemIndex = (currentItemIndex + 1) % loadingText.size
    }

    val currentItem = loadingText[currentItemIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.loading_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Spacer(
            modifier = Modifier
                .size(14.dp)
        )

        Text(
            text = stringResource(currentItem),
            style = MaterialTheme.typography.bodyMedium,
        )
    }

}

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.search_error_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Spacer(
            modifier = Modifier
                .size(14.dp)
        )

        Text(
            text = stringResource(R.string.search_error_message),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun IdleView(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier)
}
