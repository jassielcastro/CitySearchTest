package com.ajcm.citysearch.ui.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.R
import com.ajcm.data_source_manager.repository.model.City
import com.ajcm.data_source_manager.repository.model.Coordinate

@Composable
fun CityItemView(
    city: City,
    onClick: (id: Int) -> Unit,
    onFavoriteClicked: (id: Int) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth(),
        onClick = { onClick(city.id) },
        shadowElevation = 4.dp,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(48.dp)
                    .rotate((city.id % 4) * 90f),
                imageVector = ImageVector.vectorResource(R.drawable.ic_map_square),
                contentDescription = "Location Icon",
                tint = MaterialTheme.colorScheme.onSurface
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "${city.cityName}, ${city.countryCode}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

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

            IconButton(
                onClick = { onFavoriteClicked(city.id) },
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = if (city.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview
@Composable
fun CityItemViewPreview() {
    CityItemView(
        city = City(
            countryCode = "US",
            cityName = "New York",
            id = 1,
            coordinates = Coordinate(latitude = 40.7128, longitude = -74.0060),
            favorite = false
        ),
        onClick = {},
        onFavoriteClicked = {}
    )
}
