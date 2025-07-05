package com.ajcm.citysearch.ui.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.R

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