package com.omise.omisetest.screens.charities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.omise.omisetest.MainCoroutineRule
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.getOrAwaitValue
import com.omise.omisetest.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class CharitiesViewModelTest {
    private lateinit var charitiesRepository: CharitiesRepository
    private lateinit var charitiesViewModel: CharitiesViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val charities = mutableListOf(
        Charity(id = 1, name = "Charity 1", logoUrl = "https://abc.com/logo1.png"),
        Charity(id = 2, name = "Charity 2", logoUrl = "https://abc.com/logo2.png"),
        Charity(id = 3, name = "Charity 3", logoUrl = "https://abc.com/logo3.png")
    )

    @Before
    fun setUp() {
        charitiesRepository = mock()
        charitiesViewModel = CharitiesViewModel(ApplicationProvider.getApplicationContext(), charitiesRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadCharities_whenCalled_shouldToggleLoadingProperly() = mainCoroutineRule.runBlockingTest {
        `when`(charitiesRepository.getCharities()).thenReturn(charities)
        mainCoroutineRule.pauseDispatcher()
        charitiesViewModel.loadCharities()
        Thread.sleep(100)
        charitiesViewModel.isLoading.observeForTesting {
            assertThat(charitiesViewModel.isLoading.getOrAwaitValue(), `is`(false))
            mainCoroutineRule.resumeDispatcher()
            assertThat(charitiesViewModel.isLoading.getOrAwaitValue(), `is`(true))
        }
    }

    @Test
    fun loadCharities_whenCalled_shouldLoadDataProperly() = runBlockingTest {
        `when`(charitiesRepository.getCharities()).thenReturn(charities)
        charitiesViewModel.loadCharities()
        Thread.sleep(200)
        charitiesViewModel.charities.observeForTesting {
            assertThat(charitiesViewModel.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(charitiesViewModel.charities.getOrAwaitValue().size, `is`(3))
            assertThat(charitiesViewModel.status.getOrAwaitValue(), `is`(ApiStatus.Success))
        }

        assertThat(charitiesViewModel.navigateToDonation.value, `is`(false))
        charitiesViewModel.onCharityClicked(1)
        assertThat(charitiesViewModel.selectedCharity.value, `is`(charities[0]))
        assertThat(charitiesViewModel.navigateToDonation.value, `is`(true))
    }

    @Test
    fun loadCharities_whenThrowsIOException_shouldNavigateToConnectionError() = mainCoroutineRule.runBlockingTest {
        `when`(charitiesRepository.getCharities()).doAnswer { throw IOException("Test exception") }
        charitiesViewModel.loadCharities()
        charitiesViewModel.charities.observeForTesting {
            assertThat(charitiesViewModel.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(charitiesViewModel.charities.getOrAwaitValue().size, `is`(0))
            assertThat(charitiesViewModel.status.getOrAwaitValue(), `is`(ApiStatus.NoInternet))
            assertThat(charitiesViewModel.navigateToConnectionError.getOrAwaitValue(), `is`(true))
        }
    }

    @Test
    fun loadCharities_whenThrowsIOException_shouldNavigateToServerError() = mainCoroutineRule.runBlockingTest {
        `when`(charitiesRepository.getCharities()).doAnswer { throw Exception("Test exception") }
        charitiesViewModel.loadCharities()
        Thread.sleep(100)
        charitiesViewModel.charities.observeForTesting {
            assertThat(charitiesViewModel.isLoading.getOrAwaitValue(), `is`(false))
            assertThat(charitiesViewModel.charities.getOrAwaitValue().size, `is`(0))
            assertThat(charitiesViewModel.status.getOrAwaitValue(), `is`(ApiStatus.ERROR))
            assertThat(charitiesViewModel.navigateToServerError.getOrAwaitValue(), `is`(true))
        }
    }

    @After
    fun resetMocks() {
        Mockito.reset(charitiesRepository)
    }
}