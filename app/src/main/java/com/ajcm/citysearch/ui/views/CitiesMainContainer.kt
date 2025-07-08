package com.ajcm.citysearch.ui.views

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        startDestination = Search,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable<Search>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { _ ->
            SearchListView(
                onCitySelected = { cityId ->
                    navController.navigate(CityDetails(cityId))
                }
            )
        }

        composable<CityDetails>(
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) { navBackStackEntry ->
            val details = navBackStackEntry.toRoute<CityDetails>()
            CityDetailsView(
                cityId = details.cityId,
            ) {
                navController.navigateUp()
            }
        }
    }
}
