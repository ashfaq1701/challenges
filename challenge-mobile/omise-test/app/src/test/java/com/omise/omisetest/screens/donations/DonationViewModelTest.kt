package com.omise.omisetest.screens.donations

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.omise.omisetest.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DonationViewModelTest {
    private lateinit var donationRepository: DonationRepository

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


}