package com.ajcm.citysearch.ui.views.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.ui.model.City
import com.ajcm.citysearch.ui.model.Coordinate
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.citysearch.ui.views.components.ErrorView
import com.ajcm.citysearch.ui.views.components.IdleView
import com.ajcm.citysearch.ui.views.components.LoadCityView
import org.koin.androidx.compose.koinViewModel

@Composable
fun CityDetailsView(
    cityId: Int,
    viewModel: CityDetailsViewModel = koinViewModel(),
    onBackPressed: () -> Unit
) {
    val cityDetailsState by viewModel.cityDetailsState.collectAsState()

    LaunchedEffect(cityId) {
        viewModel.loadCityDetails(cityId)
    }

    AnimatedContent(
        targetState = cityDetailsState
    ) { state ->
        when (state) {
            is UiState.Loading -> {
                LoadCityView()
            }

            is UiState.Failure -> {
                ErrorView()
            }

            is UiState.Success -> {
                CityDetailsView(
                    city = state.data,
                    onFavoriteClicked = { id ->
                        viewModel.updateFavorite(
                            cityId = id,
                            isFavorite = !state.data.favorite
                        )
                    },
                    onBackPressed = onBackPressed
                )
            }

            is UiState.Idle -> {
                IdleView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailsView(
    city: City,
    onFavoriteClicked: (id: Int) -> Unit,
    onBackPressed: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(city.favorite) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back Icon"
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                text = "${city.cityName}, ${city.countryCode}",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = {
                    onFavoriteClicked(city.id)
                    isFavorite = !isFavorite
                },
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(12.dp),
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                tint = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${city.coordinates.latitude}, ${city.coordinates.longitude}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityDetailsViewPreview() {
    CityDetailsView(
        city = City(
            countryCode = "US",
            cityName = "New York",
            coordinates = Coordinate(40.7128, -74.0060),
            favorite = true,
            id = 1
        ),
        onFavoriteClicked = {},
        onBackPressed = { /* Handle back press */ }
    )
}
