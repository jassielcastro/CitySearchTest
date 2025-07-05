package com.ajcm.citysearch.ui.views.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.R
import com.ajcm.citysearch.ui.theme.CitySearchTheme

@Composable
fun SearchBarView(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onFilterFavorites: (Boolean) -> Unit
) {
    var filterFavorites by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchFieldBarView(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(start = 16.dp, end = 8.dp)
                .fillMaxWidth(0.82f),
            onSearch = onSearch
        )

        Surface (
            modifier = Modifier
                .size(52.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
            shape = MaterialTheme.shapes.medium,
        ) {
            IconButton(
                onClick = {
                    filterFavorites = !filterFavorites
                    onFilterFavorites(filterFavorites)
                },
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = if (filterFavorites) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "Clear Icon",
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
fun SearchFieldBarView(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = modifier,
        shadowElevation = 4.dp,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = Icons.Default.Search,
                    contentDescription = "Location Icon",
                    tint = MaterialTheme.colorScheme.surface
                )
            }

            VerticalDivider(
                modifier = Modifier
                    .height(52.dp),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            )

            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(0.83f)
                    .padding(horizontal = 8.dp),
                value = text,
                onValueChange = {
                    text = it
                    onSearch(it)
                },
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = stringResource(R.string.search_hint),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    innerTextField()
                }
            )

            if (!text.isEmpty()) {
                IconButton(
                    onClick = {
                        text = ""
                        onSearch(text)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarViewPreview() {
    CitySearchTheme {
        SearchBarView(
            modifier = Modifier.fillMaxWidth(),
            onSearch = {},
            onFilterFavorites = {}
        )
    }
}
