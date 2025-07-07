package com.ajcm.citysearch.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.ui.views.CitiesMainContainer
import com.ajcm.citysearch.ui.views.map.MapContainerView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val isKeyboardOpen by keyboardAsState()

    val orientation = LocalConfiguration.current.orientation

    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen) {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.expand()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(WindowInsets.systemBars)
            .consumeWindowInsets(WindowInsets.navigationBars)
    ) {
        MapContainerView(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }

        Box (
            modifier = Modifier
                .consumeWindowInsets(WindowInsets.ime)
                .padding(top = 64.dp)
                .padding(horizontal = 8.dp)
                .fillMaxWidth(
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) 1f else 0.45f
                )
                .align(
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) Alignment.BottomCenter
                    else Alignment.CenterStart
                ),
        ) {
            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetContainerColor = MaterialTheme.colorScheme.background,
                sheetShadowElevation = 8.dp,
                sheetPeekHeight = 320.dp,
                sheetContent = {
                    CitiesMainContainer()
                },
                content = {}
            )
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val ime = WindowInsets.ime
    val isImeVisible = ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
