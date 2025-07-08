package com.ajcm.citysearch

import com.ajcm.citysearch.ui.model.City
import com.ajcm.citysearch.ui.model.Coordinate
import com.ajcm.citysearch.ui.views.SharedLocationViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharedLocationViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var sharedLocationViewModel: SharedLocationViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        sharedLocationViewModel = SharedLocationViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `given a null city, when updateSelectedCity is called, then selectedCity is updated to null`() {
        val city: City? = null

        sharedLocationViewModel.updateSelectedCity(city)

        assertThat(sharedLocationViewModel.selectedCity.value).isNull()
    }

    @Test
    fun `given a valid city, when updateSelectedCity is called, then selectedCity is updated to the given city`() {
        val city = City(
            countryCode = "FR",
            cityName = "Paris",
            id = 1,
            coordinates = Coordinate(
                longitude = 2.3,
                latitude = 48.8
            ),
            favorite = true,
        )

        sharedLocationViewModel.updateSelectedCity(city)

        assertThat(sharedLocationViewModel.selectedCity.value).isEqualTo(city)
    }

    @Test
    fun `given an initial city and then a different city, when updateSelectedCity is called multiple times, then selectedCity reflects the latest update`() {
        val initialCity = City(
            countryCode = "FR",
            cityName = "Paris",
            id = 1,
            coordinates = Coordinate(
                longitude = 2.3,
                latitude = 48.8
            ),
            favorite = true,
        )
        val newCity = City(
            countryCode = "MX",
            cityName = "Mexico City",
            id = 1,
            coordinates = Coordinate(
                longitude = 2.3,
                latitude = 48.8
            ),
            favorite = false,
        )

        sharedLocationViewModel.updateSelectedCity(initialCity)
        assertThat(sharedLocationViewModel.selectedCity.value).isEqualTo(initialCity)

        sharedLocationViewModel.updateSelectedCity(newCity)

        assertThat(sharedLocationViewModel.selectedCity.value).isEqualTo(newCity)
    }
}