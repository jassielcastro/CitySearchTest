package com.ajcm.citysearch.ui.views.details

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ajcm.citysearch.R
import com.ajcm.citysearch.ui.model.City
import com.ajcm.citysearch.ui.model.CityWater
import com.ajcm.citysearch.ui.model.SunInfo
import com.ajcm.citysearch.ui.model.TempInfo
import com.ajcm.citysearch.ui.model.WeatherDescription
import com.ajcm.citysearch.ui.theme.CitySearchTheme
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.citysearch.ui.views.components.ErrorView
import com.ajcm.citysearch.ui.views.components.IdleView
import com.ajcm.citysearch.ui.views.components.LoadCityView
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun CityDetailsView(
    cityId: Int,
    viewModel: CityDetailsViewModel = koinViewModel(),
    onBackPressed: () -> Unit
) {
    val selectedCity by viewModel.selectedCity.collectAsState()

    LaunchedEffect(cityId) {
        viewModel.loadCity(cityId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        selectedCity?.let { city ->
            CityHeader(
                city = city,
                onFavoriteClicked = { id ->
                    viewModel.updateFavorite(
                        cityId = id,
                        isFavorite = !city.favorite
                    )
                },
                onBackPressed = onBackPressed
            )

            LoadCityDetails(
                viewModel = viewModel,
                coordinates = city.coordinates.longitude to city.coordinates.latitude,
                cityName = city.cityName
            )
        }
    }
}

@Composable
fun CityHeader(
    city: City,
    onFavoriteClicked: (id: Int) -> Unit,
    onBackPressed: () -> Unit
) {
    var isFavorite by rememberSaveable { mutableStateOf(city.favorite) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
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
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
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
                tint = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun LoadCityDetails(
    viewModel: CityDetailsViewModel,
    cityName: String,
    coordinates: Pair<Double, Double>
) {
    val cityDetailsState by viewModel.cityDetailsState.collectAsState()

    LaunchedEffect(coordinates) {
        viewModel.loadCityDetails(coordinates)
    }

    AnimatedContent(
        modifier = Modifier
            .fillMaxSize(),
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
                    cityName = cityName,
                    cityWeather = state.data
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
    cityName: String,
    cityWeather: CityWater
) {
    val sunrise = cityWeather.sunInfo.sunrise + cityWeather.timezone
    val sunset = cityWeather.sunInfo.sunset + cityWeather.timezone

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                text = stringResource(
                    id = R.string.city_intro_title, cityName
                ),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        item {
            HorizontalUncontainedCarousel(
                state = rememberCarouselState { cityWeather.images.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(bottom = 16.dp),
                itemWidth = 186.dp,
                itemSpacing = 8.dp,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) { index ->
                val image = cityWeather.images[index]
                AsyncImage(
                    modifier = Modifier
                        .height(205.dp)
                        .maskClip(MaterialTheme.shapes.extraLarge),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.city_details_temperature_title),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        item {
            TemperatureItemList(cityWeather.tempInfo)
        }

        item {
            Text(
                text = stringResource(R.string.city_details_sun_title),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        item {
            SunWeatherText(
                sunrise = formatUnixTime(
                    sunrise.toLong(),
                    cityWeather.timezone
                ),
                sunset = formatUnixTime(
                    sunset.toLong(),
                    cityWeather.timezone
                ),
                weatherType = cityWeather.currentWeather.first().main,
                weatherDescription = cityWeather.currentWeather.first().description,
            )
        }
    }
}

@Composable
fun TemperatureItemList(
    temperature: TempInfo
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        temperatureItem(
            label = { stringResource(R.string.city_temperature_current) },
            value = temperature.temperature,
            alphaBackground = 1f,
            textColor = { MaterialTheme.colorScheme.surface }
        )

        temperatureItem(
            label = { stringResource(R.string.city_temperature_feels) },
            value = temperature.feelsLike,
            alphaBackground = 0.15f,
            textColor = { MaterialTheme.colorScheme.surfaceVariant }
        )

        temperatureItem(
            label = { stringResource(R.string.city_temperature_min) },
            value = temperature.tempMin,
            alphaBackground = 0.15f,
            textColor = { MaterialTheme.colorScheme.surfaceVariant }
        )

        temperatureItem(
            label = { stringResource(R.string.city_temperature_max) },
            value = temperature.tempMax,
            alphaBackground = 0.15f,
            textColor = { MaterialTheme.colorScheme.surfaceVariant }
        )
    }
}

fun LazyListScope.temperatureItem(
    label: @Composable () -> String,
    value: Float,
    alphaBackground: Float = 0.25f,
    textColor: @Composable () -> Color
) {
    item {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alphaBackground)
                ),
        ) {
            Text(
                text = stringResource(R.string.city_temperature, label(), value),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp),
                color = textColor()
            )
        }
    }
}

@Composable
fun SunWeatherText(
    modifier: Modifier = Modifier,
    @StringRes textResId: Int = R.string.sun_weather_text,
    sunrise: String,
    sunset: String,
    weatherType: String,
    weatherDescription: String
) {
    val message = stringResource(
        id = textResId,
        sunrise,
        sunset,
        weatherType.replaceFirstChar { it.uppercaseChar() },
        weatherDescription.replaceFirstChar { it.uppercaseChar() }
    )

    Text(
        modifier = modifier,
        text = message,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
}

private fun formatUnixTime(unixTime: Long, timezoneShiftInSeconds: Int): String {
    val adjustedTimeMillis = (unixTime + timezoneShiftInSeconds) * 1000L
    val date = Date(adjustedTimeMillis)
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return formatter.format(date)
}

@Preview(showBackground = true)
@Composable
fun CityDetailsViewPreview() {
    CitySearchTheme {
        CityDetailsView(
            cityName = "Sample City",
            cityWeather = CityWater(
                timezone = 7200,
                currentWeather = listOf(
                    WeatherDescription(main = "Rain", description = "moderate rain")
                ),
                tempInfo = TempInfo(
                    temperature = 22.5f,
                    feelsLike = 24.0f,
                    tempMin = 20.0f,
                    tempMax = 25.0f
                ),
                sunInfo = SunInfo(
                    sunrise = 1726636384,
                    sunset = 1726680975
                ),
                images = listOf(
                    "https://picsum.photos/400/300?random=3"
                )
            ),
        )
    }
}
