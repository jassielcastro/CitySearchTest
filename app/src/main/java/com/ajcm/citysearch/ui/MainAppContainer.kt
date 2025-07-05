package com.ajcm.citysearch.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ajcm.citysearch.ui.views.CitiesMainContainer
import com.ajcm.citysearch.ui.views.SharedLocationViewModel
import com.ajcm.citysearch.ui.views.map.MapContainerView
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContainer() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val isKeyboardOpen by keyboardAsState()

    val sharedViewModel: SharedLocationViewModel = koinViewModel()

    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen) {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.expand()
            }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier
            .consumeWindowInsets(WindowInsets.ime),
        scaffoldState = scaffoldState,
        sheetContainerColor = MaterialTheme.colorScheme.background,
        sheetShadowElevation = 8.dp,
        sheetPeekHeight = 320.dp,
        sheetContent = {
            CitiesMainContainer(
                sharedLocationViewModel = sharedViewModel
            )
        },
    ) { _ ->
        MapContainerView(
            modifier = Modifier.fillMaxSize(),
            sharedViewModel = sharedViewModel
        ) {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.partialExpand()
            }
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val ime = WindowInsets.ime
    val isImeVisible = ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}
