package com.ajcm.data_source_manager

import android.util.Log
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.ajcm.data_source_manager.client.CitiesGistService
import com.ajcm.data_source_manager.client.model.CityDto
import com.ajcm.data_source_manager.client.model.CoordinateDto
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.ajcm.storage.CitiesDAO
import com.ajcm.storage.data.CityTable
import com.ajcm.storage.data.CoordinateEmb
import com.google.common.truth.Truth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CitiesRepositoryTest {

    lateinit var citiesRepository: CitiesRepository

    @MockK(relaxed = true)
    lateinit var citiesGistService: CitiesGistService

    @MockK(relaxed = true)
    lateinit var citiesDao: CitiesDAO

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false

        citiesRepository = CitiesRepository(
            citiesGistService = citiesGistService,
            citiesDao = citiesDao
        )
    }

    @Test
    fun `given app start up, when call isCitiesPopulated, validate the null value`() {
        coEvery { citiesDao.getSingle() } returns null

        val isPopulated = runBlocking { citiesRepository.areCitiesPopulated() }

        Truth.assertThat(isPopulated).isFalse()
    }

    @Test
    fun `given app start up, when call isCitiesPopulated, validate the populated value`() {
        coEvery { citiesDao.getSingle() } returns mockk()

        val isPopulated = runBlocking { citiesRepository.areCitiesPopulated() }

        Truth.assertThat(isPopulated).isTrue()
    }

    @Test
    fun `given the app starts, when user request the cities, then shows all the cities`() {
        val pagingSourceFactory = sampleCities().asPagingSourceFactory()

        coEvery { citiesDao.getCitiesBy(any(), any()) } returns pagingSourceFactory.invoke()

        val loadResult = runBlocking {
            citiesRepository.getCitiesBy(
                favorite = 0,
                prefix = "",
            ).asSnapshot()
        }

        Truth.assertThat(loadResult).hasSize(6)
    }

    @Test
    fun `given the user click, when user select a city, then return the selected city`() {
        val cityId = slot<Int>()
        coEvery { citiesDao.getCityById(capture(cityId)) } answers {
            sampleCities().first { it.id == cityId.captured }
        }

        val city = runBlocking {
            citiesRepository.getCityById(
                cityId = 707860
            )
        }

        Truth.assertThat(city?.id).isEqualTo(707860)
        Truth.assertThat(city?.cityName).isEqualTo("Hurzuf")
        Truth.assertThat(city?.countryCode).isEqualTo("UA")
        Truth.assertThat(city?.favorite).isTrue()
    }

    @Test
    fun `given the user click, when an error happen in the ID, then return null`() {
        val cityId = slot<Int>()
        coEvery { citiesDao.getCityById(capture(cityId)) } answers {
            sampleCities().firstOrNull { it.id == cityId.captured }
        }

        val city = runBlocking {
            citiesRepository.getCityById(
                cityId = 1
            )
        }

        Truth.assertThat(city).isNull()
    }

    @Test
    fun `given the user click, when user updates the favorite status, then update the city`() {
        val cityId = slot<Int>()
        val isFavorite = slot<Boolean>()
        coEvery { citiesDao.updateFavorite(capture(cityId), capture(isFavorite)) } just runs

        val cityIdToUpdate = 707860
        val isFavoriteToUpdate = false
        runBlocking {
            citiesRepository.updateFavorite(
                cityId = cityIdToUpdate,
                isFavorite = isFavoriteToUpdate
            )
        }

        Truth.assertThat(cityIdToUpdate).isEqualTo(cityId.captured)
        Truth.assertThat(isFavoriteToUpdate).isEqualTo(isFavorite.captured)
    }

    @Test
    fun `given the user starts, when user fetches cities from remote, then save them in the database`() {
        val cities = sampleServiceCities()
        val successResponse = Response.success(cities)
        coEvery {
            citiesGistService.getCitiesData(any())
        } returns successResponse
        coEvery { citiesDao.insertCities(any()) } just runs

        val result = runBlocking {
            citiesRepository.fetchCitiesFromRemote()
        }

        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `given the user starts, when remote service return empty, then return Error`() {
        val cities = emptyList<CityDto>()
        val successResponse = Response.success(cities)
        coEvery {
            citiesGistService.getCitiesData(any())
        } returns successResponse

        val result = runBlocking {
            citiesRepository.fetchCitiesFromRemote()
        }

        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `given the user starts, when remote service fails, then return Error`() {
        val errorResponse =
            Response.error<List<CityDto>>(500, "Internal Server Error".toResponseBody())
        coEvery {
            citiesGistService.getCitiesData(any())
        } returns errorResponse

        val result = runBlocking {
            citiesRepository.fetchCitiesFromRemote()
        }

        Truth.assertThat(result.isFailure).isTrue()
    }

    private fun sampleCities() = listOf(
        CityTable(707860, "UA", "Hurzuf", CoordinateEmb(34.28, 44.55), favorite = true),
        CityTable(708546, "UA", "Holubynka", CoordinateEmb(34.28, 44.55), favorite = true),
        CityTable(
            1862845,
            "JP",
            "Higashi-asahimachi",
            CoordinateEmb(34.28, 44.55),
            favorite = false
        ),
        CityTable(707861, "RU", "Novinki", CoordinateEmb(30.52, 50.45), favorite = false),
        CityTable(707862, "NP", "Gorkhā", CoordinateEmb(36.23, 49.99), favorite = true),
        CityTable(707863, "IN", "State of Haryāna", CoordinateEmb(21.01, 52.23), favorite = false)
    )

    private fun sampleServiceCities() = listOf(
        CityDto(
            id = 707860,
            countryCode = "UA",
            cityName = "Hurzuf",
            coordinates = CoordinateDto(34.28, 44.55)
        ),
        CityDto(
            id = 708546,
            countryCode = "UA",
            cityName = "Holubynka",
            coordinates = CoordinateDto(34.28, 44.55)
        ),
        CityDto(
            id = 1862845,
            countryCode = "JP",
            cityName = "Higashi-asahimachi",
            coordinates = CoordinateDto(34.28, 44.55)
        ),
        CityDto(
            id = 707861,
            countryCode = "RU",
            cityName = "Novinki",
            coordinates = CoordinateDto(30.52, 50.45)
        ),
        CityDto(
            id = 707862,
            countryCode = "NP",
            cityName = "Gorkhā",
            coordinates = CoordinateDto(36.23, 49.99)
        ),
        CityDto(
            id = 707863,
            countryCode = "IN",
            cityName = "State of Haryāna",
            coordinates = CoordinateDto(21.01, 52.23)
        )
    )
}
