package com.omise.omisetest.screens.charities

import com.omise.omisetest.common.network.Charity
import com.omise.omisetest.common.models.Charity as DomainCharity
import com.omise.omisetest.common.network.DonationsApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import timber.log.Timber

@ExperimentalCoroutinesApi
class CharitiesRepositoryTest {
    private val charitiesResponse = mutableListOf(
        Charity(id = 1, name = "Charity 1", logoUrl = "https://abc.com/logo1.png"),
        Charity(id = 2, name = "Charity 2", logoUrl = "https://abc.com/logo2.png"),
        Charity(id = 3, name = "Charity 1", logoUrl = "https://abc.com/logo3.png")
    )

    private lateinit var donationsApiService: DonationsApiService
    private lateinit var charitiesRepository: CharitiesRepository

    @Before
    fun setUp() {
        donationsApiService = mock(DonationsApiService::class.java)
        charitiesRepository = CharitiesRepository(donationsApiService)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getCharities_withData_returnsCorrectData() = runBlockingTest {
        `when`(donationsApiService.getCharities()).thenReturn(charitiesResponse)
        val charities = charitiesRepository.getCharities()
        assertThat(charities.size, `is`(3))
        assertThat(charities.first(), instanceOf(DomainCharity::class.java))
    }

    @Test
    @ExperimentalCoroutinesApi
    fun getCharities_withEmptyList_returnsEmptyList() = runBlockingTest {
        `when`(donationsApiService.getCharities()).thenReturn(mutableListOf())
        val charities = charitiesRepository.getCharities()
        assertThat(charities.size, `is`(0))
    }

    @After
    fun resetMocks() {
        reset(donationsApiService)
    }

}