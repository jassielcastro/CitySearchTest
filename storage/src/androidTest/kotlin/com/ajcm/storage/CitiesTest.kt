package com.ajcm.storage

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ajcm.storage.data.CityTable
import com.ajcm.storage.data.CoordinateEmb
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CitiesTest {

    private lateinit var db: CitiesDataBase
    private lateinit var dao: CitiesDAO

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            CitiesDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = db.citiesDao()
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    private fun sampleCities() = listOf(
        CityTable(707860, "UA", "Hurzuf", CoordinateEmb(34.28, 44.55), favorite = true),
        CityTable(708546, "UA", "Holubynka", CoordinateEmb(34.28, 44.55), favorite = true),
        CityTable(1862845, "JP", "Higashi-asahimachi", CoordinateEmb(34.28, 44.55), favorite = false),
        CityTable(707861, "RU", "Novinki", CoordinateEmb(30.52, 50.45), favorite = false),
        CityTable(707862, "NP", "Gorkhā", CoordinateEmb(36.23, 49.99), favorite = true),
        CityTable(707863, "IN", "State of Haryāna", CoordinateEmb(21.01, 52.23), favorite = false)
    )

    @Test
    fun validateNoRecords() = runBlocking {
        val recordsExist = dao.getSingle()

        assertEquals(recordsExist, null)
    }

    @Test
    fun insertAndValidateNotEmptyResults() = runBlocking {
        dao.insertCities(sampleCities())

        val recordsExist = dao.getSingle()

        assertEquals(recordsExist?.id, 707860)
    }

    @Test
    fun getAllCitiesWithoutPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val pagingSource = dao.getCitiesBy(favorite = 0, prefix = "")
        val loadResult = pagingSource.toLoad().toData()

        assertEquals(6, loadResult.size)
    }

    @Test
    fun getOnlyFavoritesCitiesWithoutPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val pagingSource = dao.getCitiesBy(favorite = 1, prefix = "")
        val actual = pagingSource.toLoad().toData()

        assertEquals(3, actual.size)
        assertTrue(actual.all { it.favorite })
    }

    @Test
    fun getOnlyFavoritesCitiesWithPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val pagingSource = dao.getCitiesBy(favorite = 1, prefix = "h")
        val actual = pagingSource.toLoad().toData()

        assertEquals(2, actual.size)
        assertEquals("Holubynka", actual[0].name)
        assertEquals("Hurzuf", actual[1].name)
    }

    @Test
    fun getAllCitiesOnlyWithPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val pagingSource = dao.getCitiesBy(favorite = 0, prefix = "h")
        val actual = pagingSource.toLoad().toData()

        assertEquals(3, actual.size)
        assertEquals("Higashi-asahimachi", actual[0].name)
        assertEquals("Holubynka", actual[1].name)
        assertEquals("Hurzuf", actual[2].name)
    }

    @Test
    fun getNoResults() = runBlocking {
        dao.insertCities(sampleCities())

        val pagingSource = dao.getCitiesBy(favorite = 0, prefix = "a")
        val actual = pagingSource.toLoad().toData()
        assertEquals(0, actual.size)
    }

    @Test
    fun pagingQueryReturnsCorrectPage() = runBlocking {
        dao.insertCities(sampleCities())

        val page0 = dao.getCitiesBy(favorite = 0, prefix = "")
        val loadResult0 = page0.toLoad(
            loadSize = 2
        ).toData()

        val page1 = dao.getCitiesBy(favorite = 0, prefix = "")
        val loadResult1 = page1.toLoad(
            key = 1,
            loadSize = 2
        ).toData()

        assertEquals(2, loadResult0.size)
        assertEquals(2, loadResult1.size)
        assertNotEquals(loadResult0[0].id, loadResult1[0].id)
    }

    @Test
    fun updateFavoriteStatus() = runBlocking {
        dao.insertCities(sampleCities())

        val cityId = 707860 // Hurzuf
        dao.updateFavorite(cityId, false)

        val updatedCity = dao.getCityById(cityId)
        assertEquals(false, updatedCity?.favorite)
    }

    @Test
    fun getCityById() = runBlocking {
        dao.insertCities(sampleCities())

        val cityId = 707860 // Hurzuf
        val city = dao.getCityById(cityId)

        assertEquals(cityId, city?.id)
        assertEquals("Hurzuf", city?.name)
        assertEquals("UA", city?.country)
        assertEquals(CoordinateEmb(34.28, 44.55), city?.coordinate)
        assertTrue(city?.favorite == true)
    }

    @Test
    fun getCityByIdThatDoesNotExist() = runBlocking {
        dao.insertCities(sampleCities())

        val cityId = 1
        val city = dao.getCityById(cityId)

        assertNull(city)
    }

    private suspend fun <T : Any, C : Any> PagingSource<T, C>.toLoad(
        key: T? = null,
        loadSize: Int = 6
    ): PagingSource.LoadResult<T, C> {
        return this.load(
            PagingSource.LoadParams.Refresh(
                key = key,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        )
    }

    private fun <T : Any, C : Any> PagingSource.LoadResult<T, C>.toData(): List<C> {
        return when (this) {
            is PagingSource.LoadResult.Page -> this.data
            is PagingSource.LoadResult.Error -> emptyList()
            is PagingSource.LoadResult.Invalid -> emptyList()
        }
    }
}
