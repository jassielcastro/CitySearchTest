package com.ajcm.storage

import android.content.Context
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
    fun insertAndQueryAllCities() = runBlocking {
        dao.insertCities(sampleCities())

        val all = dao.getCitiesBy(favorite = false, prefix = "", limit = 10, offset = 0)

        assertEquals(all.size, 6)
    }

    @Test
    fun getOnlyFavoritesCitiesWithoutPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val favorites = dao.getCitiesBy(favorite = true, prefix = "", limit = 10, offset = 0)
        assertEquals(3, favorites.size)
        assertTrue(favorites.all { it.favorite })
    }

    @Test
    fun getOnlyFavoritesCitiesWithPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val result = dao.getCitiesBy(favorite = true, prefix = "h", limit = 10, offset = 0)
        assertEquals(2, result.size)
        assertEquals("Holubynka", result[0].name)
        assertEquals("Hurzuf", result[1].name)
    }

    @Test
    fun getAllCitiesOnlyWithPrefix() = runBlocking {
        dao.insertCities(sampleCities())

        val result = dao.getCitiesBy(favorite = false, prefix = "h", limit = 10, offset = 0)
        assertEquals(3, result.size)
        assertEquals("Higashi-asahimachi", result[0].name)
        assertEquals("Holubynka", result[1].name)
        assertEquals("Hurzuf", result[2].name)
    }

    @Test
    fun getNoResults() = runBlocking {
        dao.insertCities(sampleCities())

        val result = dao.getCitiesBy(favorite = false, prefix = "a", limit = 10, offset = 0)
        assertEquals(0, result.size)
    }

    @Test
    fun pagingQueryReturnsCorrectPage() = runBlocking {
        dao.insertCities(sampleCities())

        val page0 = dao.getCitiesBy(favorite = false, prefix = "", limit = 2, offset = 0)
        val page1 = dao.getCitiesBy(favorite = false, prefix = "", limit = 2, offset = 2)

        assertEquals(2, page0.size)
        assertEquals(2, page1.size)
        assertNotEquals(page0[0].id, page1[0].id)
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
}
