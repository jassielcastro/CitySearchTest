package com.ajcm.citysearch.ui.views.map

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ajcm.citysearch.ui.views.SharedLocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.koin.androidx.compose.koinViewModel

@Composable
fun MapContainerView(
    modifier: Modifier = Modifier,
    onZoomToCity: () -> Unit
) {
    val sharedViewModel: SharedLocationViewModel = koinViewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
    val selectedCity by sharedViewModel.selectedCity.collectAsState()

    val cameraPositionState = rememberCameraPositionState()
    var uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true)) }
    var properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }

    LaunchedEffect(selectedCity) {
        selectedCity?.let {
            onZoomToCity()
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.coordinates.latitude, it.coordinates.longitude),
                    5f
                ),
                durationMs = 1000
            )
        }
    }

    Box(modifier) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            properties = properties,
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {
            selectedCity?.let {
                Marker(
                    state = MarkerState(
                        position = LatLng(
                            selectedCity!!.coordinates.latitude,
                            selectedCity!!.coordinates.longitude
                        )
                    ),
                    title = "${selectedCity!!.cityName}, ${selectedCity!!.countryCode}",
                )
            }
        }
    }
}
