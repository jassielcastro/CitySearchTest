import com.ajcm.citysearch.ui.views.UiState
import com.ajcm.citysearch.ui.views.search.SearchViewModel
import com.ajcm.data_source_manager.repository.CitiesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var repository: CitiesRepository

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        searchViewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `given cities are not populated, when loadCities is called, then fetches from remote and emits success`() {
        coEvery { repository.areCitiesPopulated() } returns false
        coEvery { repository.fetchCitiesFromRemote() } returns Result.success(Unit)

        searchViewModel.loadCities()

        assertThat(searchViewModel.citiesState.value).isEqualTo(UiState.Success(Unit))
        coVerify(exactly = 1) { repository.areCitiesPopulated() }
        coVerify(exactly = 1) { repository.fetchCitiesFromRemote() }
    }

    @Test
    fun `given cities are not populated and remote fetch fails, when loadCities is called, then emits failure`() {
        coEvery { repository.areCitiesPopulated() } returns false
        coEvery { repository.fetchCitiesFromRemote() } returns Result.failure(Throwable("Network Error"))

        searchViewModel.loadCities()

        assertThat(searchViewModel.citiesState.value).isEqualTo(UiState.Failure)
        coVerify(exactly = 1) { repository.areCitiesPopulated() }
        coVerify(exactly = 1) { repository.fetchCitiesFromRemote() }
    }

    @Test
    fun `given cities are populated, when loadCities is called, then does not fetch from remote and emits success`() {
        coEvery { repository.areCitiesPopulated() } returns true

        searchViewModel.loadCities()

        assertThat(searchViewModel.citiesState.value).isEqualTo(UiState.Success(Unit))
        coVerify(exactly = 1) { repository.areCitiesPopulated() }
        coVerify(exactly = 0) { repository.fetchCitiesFromRemote() }
    }

    @Test
    fun `when updateSearchQuery is called, then _prefix flow is updated`() = runBlocking {
        val query = "London"

        searchViewModel.updateSearchQuery(query)

        val prefixField = searchViewModel::class.java.getDeclaredField("_prefix")
        prefixField.isAccessible = true
        val mutableStateFlow = prefixField.get(searchViewModel) as MutableStateFlow<*>
        assertThat(mutableStateFlow.first()).isEqualTo(query)
    }

    @Test
    fun `when updateSearchFavorites is called with true, then _favorite flow is updated to 1`() = runBlocking {
        val favorites = true

        searchViewModel.updateSearchFavorites(favorites)

        val favoriteField = searchViewModel::class.java.getDeclaredField("_favorite")
        favoriteField.isAccessible = true
        val mutableStateFlow = favoriteField.get(searchViewModel) as MutableStateFlow<*>
        assertThat(mutableStateFlow.first()).isEqualTo(1)
    }

    @Test
    fun `when updateSearchFavorites is called with false, then _favorite flow is updated to 0`() = runBlocking {
        val favorites = false

        searchViewModel.updateSearchFavorites(favorites)

        val favoriteField = searchViewModel::class.java.getDeclaredField("_favorite")
        favoriteField.isAccessible = true
        val mutableStateFlow = favoriteField.get(searchViewModel) as MutableStateFlow<*>
        assertThat(mutableStateFlow.first()).isEqualTo(0)
    }

    @Test
    fun `given _favorite is 1, when isFavoriteSelected is called, then returns true`() {
        searchViewModel.updateSearchFavorites(true)

        val isSelected = searchViewModel.isFavoriteSelected()

        assertThat(isSelected).isTrue()
    }

    @Test
    fun `given _favorite is 0, when isFavoriteSelected is called, then returns false`() {
        searchViewModel.updateSearchFavorites(false)

        val isSelected = searchViewModel.isFavoriteSelected()

        assertThat(isSelected).isFalse()
    }

    @Test
    fun `when updateFavorite is called, then repository updateFavorite is invoked and refreshTrigger emits Unit`() = runBlocking {
        val cityId = 123
        val isFavorite = true
        coEvery { repository.updateFavorite(cityId, isFavorite) } just Runs

        searchViewModel.updateFavorite(cityId, isFavorite)

        coVerify(exactly = 1) { repository.updateFavorite(cityId, isFavorite) }
    }
}
