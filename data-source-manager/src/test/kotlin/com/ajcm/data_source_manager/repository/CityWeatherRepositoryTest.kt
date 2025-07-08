package com.ajcm.data_source_manager.repository

import com.ajcm.data_source_manager.client.CityWaterService
import com.ajcm.data_source_manager.client.model.SunInfoDto
import com.ajcm.data_source_manager.client.model.TempInfoDto
import com.ajcm.data_source_manager.client.model.WeatherDto
import com.ajcm.data_source_manager.client.model.WeatherDescriptionDto
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CityWeatherRepositoryTest {

    @MockK(relaxed = true)
    lateinit var cityWaterService: CityWaterService

    lateinit var cityWeatherRepository: CityWeatherRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        cityWeatherRepository = CityWeatherRepository(cityWaterService)
    }

    @Test
    fun `given a Coordinates, when user click a City, then returns a City weather information`() {
        val cities = mockCityWeatherData()
        val successResponse = Response.success(cities)
        coEvery {
            cityWaterService.getCurrentCityWeatherBy(
                any(),
                any(),
                any()
            )
        } returns successResponse

        val result = runBlocking {
            cityWeatherRepository.getWeatherBy(40.7128, -74.0060, "test_api_key")
        }

        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `given a Coordinates, when user click a City, then returns a list of images`() {
        val cities = mockCityWeatherData()
        val successResponse = Response.success(cities)
        coEvery {
            cityWaterService.getCurrentCityWeatherBy(
                any(),
                any(),
                any()
            )
        } returns successResponse

        val result = runBlocking {
            cityWeatherRepository.getWeatherBy(40.7128, -74.0060, "test_api_key")
        }

        val waterData = result.getOrNull()
        Truth.assertThat(waterData?.images).isNotEmpty()
    }

    @Test
    fun `given a Coordinates, when user click a City, then returns a Weather details`() {
        val cities = mockCityWeatherData()
        val successResponse = Response.success(cities)
        coEvery {
            cityWaterService.getCurrentCityWeatherBy(
                any(),
                any(),
                any()
            )
        } returns successResponse

        val result = runBlocking {
            cityWeatherRepository.getWeatherBy(40.7128, -74.0060, "test_api_key")
        }

        val waterData = result.getOrNull()
        Truth.assertThat(waterData?.sunInfo?.sunrise).isEqualTo(1726636384)
        Truth.assertThat(waterData?.sunInfo?.sunset).isEqualTo(1726680975)

        Truth.assertThat(waterData?.tempInfo?.temperature).isEqualTo(22.5f)
        Truth.assertThat(waterData?.tempInfo?.feelsLike).isEqualTo(24.0f)
        Truth.assertThat(waterData?.tempInfo?.tempMin).isEqualTo(20.0f)
        Truth.assertThat(waterData?.tempInfo?.tempMax).isEqualTo(25.0f)
        Truth.assertThat(waterData?.currentWeather?.firstOrNull()?.main).isEqualTo("Rain")
        Truth.assertThat(waterData?.currentWeather?.firstOrNull()?.description).isEqualTo("moderate rain")
    }

    @Test
    fun `given a Coordinates, when user click a City, then returns error for by null body`() {
        val successResponse = Response.success<WeatherDto>(null)
        coEvery {
            cityWaterService.getCurrentCityWeatherBy(
                any(),
                any(),
                any()
            )
        } returns successResponse

        val result = runBlocking {
            cityWeatherRepository.getWeatherBy(40.7128, -74.0060, "test_api_key")
        }

        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `given a Coordinates, when user click a City, then returns error for by connection`() {
        coEvery {
            cityWaterService.getCurrentCityWeatherBy(
                any(),
                any(),
                any()
            )
        } throws Exception("Network error")

        val result = runBlocking {
            cityWeatherRepository.getWeatherBy(40.7128, -74.0060, "test_api_key")
        }

        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `given a Coordinates, when Coordinated does not exist, then returns mock success`() {
        val errorResponse =
            Response.error<WeatherDto>(400, "wrong latitude".toResponseBody())
        coEvery {
            cityWaterService.getCurrentCityWeatherBy(
                any(),
                any(),
                any()
            )
        } returns errorResponse

        val result = runBlocking {
            cityWeatherRepository.getWeatherBy(40.7128, -74.0060, "test_api_key")
        }

        val waterData = result.getOrNull()
        Truth.assertThat(waterData?.sunInfo?.sunrise).isEqualTo(1726636384)
        Truth.assertThat(waterData?.sunInfo?.sunset).isEqualTo(1726680975)

        Truth.assertThat(waterData?.tempInfo?.temperature).isEqualTo(22.5f)
        Truth.assertThat(waterData?.tempInfo?.feelsLike).isEqualTo(24.0f)
        Truth.assertThat(waterData?.tempInfo?.tempMin).isEqualTo(20.0f)
        Truth.assertThat(waterData?.tempInfo?.tempMax).isEqualTo(25.0f)
        Truth.assertThat(waterData?.currentWeather?.firstOrNull()?.main).isEqualTo("Rain")
        Truth.assertThat(waterData?.currentWeather?.firstOrNull()?.description).isEqualTo("moderate rain")
        Truth.assertThat(waterData?.images).isNotEmpty()
    }

    private fun mockCityWeatherData(): WeatherDto {
        return WeatherDto(
            timezone = 7200,
            weather = listOf(
                WeatherDescriptionDto(
                    main = "Rain",
                    description = "moderate rain"
                )
            ),
            tempInfo = TempInfoDto(
                temperature = 22.5f,
                feelsLike = 24.0f,
                tempMin = 20.0f,
                tempMax = 25.0f
            ),
            sunInfo = SunInfoDto(
                sunrise = 1726636384,
                sunset = 1726680975
            ),
        )
    }

}
