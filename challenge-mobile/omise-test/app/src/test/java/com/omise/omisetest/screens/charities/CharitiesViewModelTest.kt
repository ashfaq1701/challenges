package com.omise.omisetest.screens.charities

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.omise.omisetest.MainCoroutineRule
import com.omise.omisetest.common.models.Charity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CharitiesViewModelTest {
    private lateinit var charitiesRepository: CharitiesRepository
    private lateinit var charitiesViewModel: CharitiesViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

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


}