package com.ajcm.citysearch.ui.views.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.R
import kotlinx.coroutines.delay

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
