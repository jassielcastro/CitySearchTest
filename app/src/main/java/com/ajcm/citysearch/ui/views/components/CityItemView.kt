package com.ajcm.citysearch.ui.views.components

import androidx.compose.foundation.layout.Arrangement
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
import com.ajcm.citysearch.ui.model.City
import com.ajcm.citysearch.ui.model.Coordinate
import com.ajcm.citysearch.ui.theme.CitySearchTheme

@Composable
fun CityItemView(
    city: City,
    onClick: (id: Int) -> Unit,
    onFavoriteClicked: (id: Int) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        onClick = { onClick(city.id) },
        color = MaterialTheme.colorScheme.background,
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
                tint = MaterialTheme.colorScheme.surfaceVariant
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
                        tint = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f)
                    )

                    Text(
                        text = "${city.coordinates.latitude}, ${city.coordinates.longitude}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
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
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityItemViewPreview() {
    CitySearchTheme {
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
}
