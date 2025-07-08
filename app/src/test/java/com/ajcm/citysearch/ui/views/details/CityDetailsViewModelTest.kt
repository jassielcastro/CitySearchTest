package com.ajcm.citysearch.ui.views.details

import com.ajcm.citysearch.ui.mapper.toCity
import com.ajcm.citysearch.ui.mapper.toCityWater
import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.ajcm.data_source_manager.repository.CityWeatherRepository
import com.ajcm.data_source_manager.repository.model.CityData
import com.ajcm.data_source_manager.repository.model.CityWeatherData
import com.ajcm.data_source_manager.repository.model.CoordinateData
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CityDetailsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var citiesRepository: CitiesRepository

    @MockK
    private lateinit var cityWeatherRepository: CityWeatherRepository

    private lateinit var cityDetailsViewModel: CityDetailsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        cityDetailsViewModel = CityDetailsViewModel(citiesRepository, cityWeatherRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `given no city selected, when loadCity is called with a valid cityId, then selectedCity is updated`() {
        val cityId = 1
        val cityData = CityData(
            countryCode = "FR",
            cityName = "Paris",
            id = 1,
            coordinates = CoordinateData(
                longitude = 2.3,
                latitude = 48.8
            ),
            favorite = true,
        )
        coEvery { citiesRepository.getCityById(cityId) } returns cityData

        cityDetailsViewModel.loadCity(cityId)

        Truth.assertThat(cityDetailsViewModel.selectedCity.value?.cityName).isEqualTo("Paris")
        Truth.assertThat(cityDetailsViewModel.selectedCity.value?.countryCode).isEqualTo("FR")
        Truth.assertThat(cityDetailsViewModel.selectedCity.value?.favorite).isTrue()
        Truth.assertThat(cityDetailsViewModel.selectedCity.value?.coordinates?.longitude).isEqualTo(2.3)
        Truth.assertThat(cityDetailsViewModel.selectedCity.value?.coordinates?.latitude).isEqualTo(48.8)
        coVerify(exactly = 1) { citiesRepository.getCityById(cityId) }
    }

    @Test
    fun `given a city is already selected, when loadCity is called, then selectedCity is not updated and repository is not called`() {
        val cityId = 1
        val preSelectedCity = CityData(
            countryCode = "FR",
            cityName = "Paris",
            id = 1,
            coordinates = CoordinateData(
                longitude = 2.3,
                latitude = 48.8
            ),
            favorite = true,
        ).toCity()
        cityDetailsViewModel.selectedCity.value = preSelectedCity

        cityDetailsViewModel.loadCity(cityId)

        Truth.assertThat(cityDetailsViewModel.selectedCity.value).isEqualTo(preSelectedCity)
        coVerify(exactly = 0) { citiesRepository.getCityById(any()) }
    }

    @Test
    fun `given city not found, when loadCity is called, then selectedCity remains null`() {
        val cityId = 99
        coEvery { citiesRepository.getCityById(cityId) } returns null

        cityDetailsViewModel.loadCity(cityId)

        Truth.assertThat(cityDetailsViewModel.selectedCity.value).isNull()
        coVerify(exactly = 1) { citiesRepository.getCityById(cityId) }
    }

    @Test
    fun `given idle state, when loadCityDetails is called with valid coordinates and successful response, then emits loading and then success state`() {
        val coordinates = Pair(51.5, -0.1)
        val weatherData = mockk<CityWeatherData>(relaxed = true)
        val expectedCityWater = weatherData.toCityWater()

        coEvery {
            cityWeatherRepository.getWaterBy(any(), any(), any())
        } returns Result.success(weatherData)

        cityDetailsViewModel.loadCityDetails(coordinates)

        Truth.assertThat(cityDetailsViewModel.cityDetailsState.value).isEqualTo(
            UiState.Success(
                expectedCityWater
            )
        )
        coVerify(exactly = 1) { cityWeatherRepository.getWaterBy(any(), any(), any()) }
    }

    @Test
    fun `given idle state, when loadCityDetails is called with valid coordinates and failed response, then emits loading and then failure state`() {
        val coordinates = Pair(51.5, -0.1)
        val errorMessage = "API Error"
        coEvery {
            cityWeatherRepository.getWaterBy(any(), any(), any())
        } returns Result.failure(Exception(errorMessage))

        cityDetailsViewModel.loadCityDetails(coordinates)

        Truth.assertThat(cityDetailsViewModel.cityDetailsState.value).isEqualTo(UiState.Failure)
        coVerify(exactly = 1) { cityWeatherRepository.getWaterBy(any(), any(), any()) }
    }

    @Test
    fun `given details state is not idle, when loadCityDetails is called, then state is not updated and repository is not called`() {
        val coordinates = Pair(51.5, -0.1)
        cityDetailsViewModel.mCityDetailsState.value = UiState.Loading // Set to a non-idle state

        cityDetailsViewModel.loadCityDetails(coordinates)

        Truth.assertThat(cityDetailsViewModel.cityDetailsState.value).isEqualTo(UiState.Loading)
        coVerify(exactly = 0) { cityWeatherRepository.getWaterBy(any(), any(), any()) }
    }

    @Test
    fun `when updateFavorite is called, then cities repository updateFavorite is invoked`() {
        val cityId = 123
        val isFavorite = true
        coEvery { citiesRepository.updateFavorite(cityId, isFavorite) } returns Unit

        cityDetailsViewModel.updateFavorite(cityId, isFavorite)

        coVerify(exactly = 1) { citiesRepository.updateFavorite(cityId, isFavorite) }
    }
}