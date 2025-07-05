package com.ajcm.citysearch.ui.views

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ajcm.citysearch.ui.CityDetails
import com.ajcm.citysearch.ui.Search
import com.ajcm.citysearch.ui.views.details.CityDetailsView
import com.ajcm.citysearch.ui.views.search.SearchListView

@Composable
fun CitiesMainContainer(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .imePadding(),
        navController = navController,
        startDestination = Search
    ) {
        composable<Search> { _ ->
            SearchListView(
                onCitySelected = { cityId ->
                    navController.navigate(CityDetails(cityId))
                }
            )
        }

        composable<CityDetails> { navBackStackEntry ->
            val details = navBackStackEntry.toRoute<CityDetails>()
            CityDetailsView(
                cityId = details.cityId,
            ) {
                navController.navigateUp()
            }
        }
    }
}
